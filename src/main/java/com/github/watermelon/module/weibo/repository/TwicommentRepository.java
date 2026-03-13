package com.github.watermelon.module.weibo.repository;

import com.github.watermelon.module.weibo.entity.Twicomment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 微博评论Repository接口
 */
@Repository
public interface TwicommentRepository extends MongoRepository<Twicomment, String> {

    /**
     * 根据微博ID查找评论
     */
    @Query("{ 'twibo.$id': ?0 }")
    List<Twicomment> findByTwiboId(String twiboId);

    /**
     * 根据微博ID查找评论（分页）
     */
    @Query("{ 'twibo.$id': ?0 }")
    Page<Twicomment> findByTwiboId(String twiboId, Pageable pageable);

    /**
     * 根据用户ID查找评论
     */
    @Query("{ 'user.$id': ?0 }")
    List<Twicomment> findByUserId(String userId);

    /**
     * 根据用户ID查找评论（分页）
     */
    @Query("{ 'user.$id': ?0 }")
    Page<Twicomment> findByUserId(String userId, Pageable pageable);

    /**
     * 根据微博ID和用户ID查找评论
     */
    @Query("{ 'twibo.$id': ?0, 'user.$id': ?1 }")
    List<Twicomment> findByTwiboIdAndUserId(String twiboId, String userId);

    /**
     * 根据微博ID删除所有评论
     */
    @Query("{ 'twibo.$id': ?0 }")
    void deleteByTwiboId(String twiboId);

    /**
     * 根据用户ID删除所有评论
     */
    @Query("{ 'user.$id': ?0 }")
    void deleteByUserId(String userId);

    /**
     * 统计微博的评论数量
     */
    @Query("{ 'twibo.$id': ?0 }")
    long countByTwiboId(String twiboId);

    /**
     * 统计用户的评论数量
     */
    @Query("{ 'user.$id': ?0 }")
    long countByUserId(String userId);

    /**
     * 查找最新的评论
     */
    List<Twicomment> findTop10ByOrderByTimestampDesc();

    /**
     * 查找微博的最新评论
     */
    List<Twicomment> findTop10ByTwiboIdOrderByTimestampDesc(String twiboId);

    /**
     * 查找用户的最新评论
     */
    List<Twicomment> findTop10ByUserIdOrderByTimestampDesc(String userId);

    /**
     * 检查微博是否有评论
     */
    @Query("{ 'twibo.$id': ?0 }")
    boolean existsByTwiboId(String twiboId);

    /**
     * 检查用户是否有评论
     */
    @Query("{ 'user.$id': ?0 }")
    boolean existsByUserId(String userId);

    /**
     * 根据ID查找评论
     */
    Optional<Twicomment> findById(String id);

    /**
     * 批量删除评论
     */
    @Query("{ 'id': { $in: ?0 } }")
    void deleteByIds(List<String> ids);
}