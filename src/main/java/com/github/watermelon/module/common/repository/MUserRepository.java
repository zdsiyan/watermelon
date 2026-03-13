package com.github.watermelon.module.common.repository;

import com.github.watermelon.module.common.entity.MUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户Repository接口
 * 替代原有的MUserDAO
 */
@Repository
public interface MUserRepository extends MongoRepository<MUser, String> {

    /**
     * 根据用户名查找用户
     */
    Optional<MUser> findByUserName(String userName);

    /**
     * 根据昵称查找用户
     */
    Optional<MUser> findByNickName(String nickName);

    /**
     * 根据邮箱查找用户
     */
    Optional<MUser> findByValidationEmail(String email);

    /**
     * 根据用户名或昵称查找用户
     */
    List<MUser> findByUserNameOrNickName(String userName, String nickName);

    /**
     * 根据用户名查找用户（忽略大小写）
     */
    Optional<MUser> findByUserNameIgnoreCase(String userName);

    /**
     * 根据昵称查找用户（忽略大小写）
     */
    Optional<MUser> findByNickNameIgnoreCase(String nickName);

    /**
     * 根据邮箱查找用户（忽略大小写）
     */
    Optional<MUser> findByValidationEmailIgnoreCase(String email);

    /**
     * 检查用户名是否存在
     */
    boolean existsByUserName(String userName);

    /**
     * 检查昵称是否存在
     */
    boolean existsByNickName(String nickName);

    /**
     * 检查邮箱是否存在
     */
    boolean existsByValidationEmail(String email);

    /**
     * 根据用户名删除用户
     */
    void deleteByUserName(String userName);

    /**
     * 根据昵称删除用户
     */
    void deleteByNickName(String nickName);

    /**
     * 查找关注了指定用户的用户列表
     * 使用自定义查询，查询follows字段中包含指定用户ID的用户
     */
    @Query("{ 'follows.$id': ?0 }")
    List<MUser> findUsersFollowingUser(String userId);

    /**
     * 查找被指定用户关注的用户列表
     * 使用自定义查询，查询fans字段中包含指定用户ID的用户
     */
    @Query("{ 'fans.$id': ?0 }")
    List<MUser> findUsersFollowedByUser(String userId);

    /**
     * 查找用户名包含指定关键词的用户
     */
    List<MUser> findByUserNameContaining(String keyword);

    /**
     * 查找昵称包含指定关键词的用户
     */
    List<MUser> findByNickNameContaining(String keyword);

    /**
     * 统计用户数量
     */
    long count();

    /**
     * 根据状态查找用户（预留字段，用于扩展）
     */
    List<MUser> findByStatus(Integer status);

    /**
     * 根据创建时间范围查找用户
     */
    List<MUser> findByCreatedAtBetween(java.time.LocalDateTime start, java.time.LocalDateTime end);

    /**
     * 根据更新时间范围查找用户
     */
    List<MUser> findByUpdatedAtBetween(java.time.LocalDateTime start, java.time.LocalDateTime end);
}