package com.github.watermelon.module.weibo.repository;

import com.github.watermelon.module.weibo.entity.Twibo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 微博Repository接口
 * 替代原有的TwiboDAO
 */
@Repository
public interface TwiboRepository extends MongoRepository<Twibo, String> {

    /**
     * 根据用户ID查找微博（分页）
     */
    Page<Twibo> findByUserId(String userId, Pageable pageable);

    /**
     * 根据用户ID和状态查找微博
     */
    List<Twibo> findByUserIdAndStatus(String userId, Integer status);

    /**
     * 根据用户ID查找正常状态的微博（分页）
     */
    Page<Twibo> findByUserIdAndStatus(String userId, Integer status, Pageable pageable);

    /**
     * 根据状态查找微博
     */
    List<Twibo> findByStatus(Integer status);

    /**
     * 根据状态查找微博（分页）
     */
    Page<Twibo> findByStatus(Integer status, Pageable pageable);

    /**
     * 查找原始微博（非转发）
     */
    List<Twibo> findByOriginalIsNull();

    /**
     * 查找转发微博
     */
    List<Twibo> findByOriginalIsNotNull();

    /**
     * 根据原始微博ID查找转发
     */
    List<Twibo> findByOriginalId(String originalId);

    /**
     * 根据原始微博ID查找转发（分页）
     */
    Page<Twibo> findByOriginalId(String originalId, Pageable pageable);

    /**
     * 根据内容关键词查找微博
     */
    List<Twibo> findByContentContaining(String keyword);

    /**
     * 根据内容关键词查找微博（分页）
     */
    Page<Twibo> findByContentContaining(String keyword, Pageable pageable);

    /**
     * 根据发布时间范围查找微博
     */
    List<Twibo> findByPublishedAtBetween(LocalDateTime start, LocalDateTime end);

    /**
     * 根据发布时间范围查找微博（分页）
     */
    Page<Twibo> findByPublishedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    /**
     * 查找用户关注的人的微博（时间线）
     * 使用自定义查询，查询用户关注的人的微博
     */
    @Query("{ 'user.$id': { $in: ?0 }, 'status': 1 }")
    Page<Twibo> findTimelineByUserIds(List<String> userIds, Pageable pageable);

    /**
     * 查找热门微博（按评论数排序）
     */
    @Query("{ 'status': 1 }")
    Page<Twibo> findHotTwibos(Pageable pageable);

    /**
     * 统计用户的微博数量
     */
    long countByUserId(String userId);

    /**
     * 统计用户正常状态的微博数量
     */
    long countByUserIdAndStatus(String userId, Integer status);

    /**
     * 统计微博的转发数量
     */
    long countByOriginalId(String originalId);

    /**
     * 根据用户ID删除所有微博
     */
    void deleteByUserId(String userId);

    /**
     * 根据用户ID和状态删除微博
     */
    void deleteByUserIdAndStatus(String userId, Integer status);

    /**
     * 查找最新的微博（按发布时间倒序）
     */
    List<Twibo> findTop10ByStatusOrderByPublishedAtDesc(Integer status);

    /**
     * 查找用户的最新微博
     */
    List<Twibo> findTop5ByUserIdAndStatusOrderByPublishedAtDesc(String userId, Integer status);

    /**
     * 检查用户是否有微博
     */
    boolean existsByUserId(String userId);

    /**
     * 检查微博是否存在且状态正常
     */
    boolean existsByIdAndStatus(String id, Integer status);

    /**
     * 根据ID查找微博（忽略状态）
     */
    Optional<Twibo> findById(String id);

    /**
     * 根据ID和状态查找微博
     */
    Optional<Twibo> findByIdAndStatus(String id, Integer status);

    /**
     * 批量更新微博状态
     */
    @Query("{ 'id': { $in: ?0 } }")
    void updateStatusByIds(List<String> ids, Integer status);
}