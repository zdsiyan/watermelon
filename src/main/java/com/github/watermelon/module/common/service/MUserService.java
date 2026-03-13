package com.github.watermelon.module.common.service;

import com.github.watermelon.core.util.PasswordEncoderUtil;
import com.github.watermelon.module.common.entity.MUser;
import com.github.watermelon.module.common.entity.Signature;
import com.github.watermelon.module.common.repository.MUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 用户服务类
 * 使用Spring Data MongoDB重构
 */
@Slf4j
@Service
@Transactional
public class MUserService {

    private final MUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordEncoderUtil legacyPasswordEncoderUtil;

    @Autowired
    public MUserService(MUserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       PasswordEncoderUtil legacyPasswordEncoderUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.legacyPasswordEncoderUtil = legacyPasswordEncoderUtil;
    }

    /**
     * 用户注册
     */
    public MUser register(MUser user) {
        // 验证输入
        if (StringUtils.isBlank(user.getUserName()) ||
            StringUtils.isBlank(user.getPassword()) ||
            StringUtils.isBlank(user.getNickName()) ||
            StringUtils.isBlank(user.getValidationEmail())) {
            throw new IllegalArgumentException("用户信息不完整");
        }

        // 检查用户名是否已存在
        if (userRepository.existsByUserName(user.getUserName())) {
            throw new IllegalArgumentException("用户名已存在");
        }

        // 检查昵称是否已存在
        if (userRepository.existsByNickName(user.getNickName())) {
            throw new IllegalArgumentException("昵称已存在");
        }

        // 检查邮箱是否已存在
        if (userRepository.existsByValidationEmail(user.getValidationEmail())) {
            throw new IllegalArgumentException("邮箱已存在");
        }

        // 加密密码（使用BCrypt）
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // 初始化其他字段
        user.setFollows(new HashSet<>());
        user.setFans(new HashSet<>());
        user.setTwiSize(0);

        // 保存用户
        return userRepository.save(user);
    }

    /**
     * 用户登录
     */
    public Optional<MUser> login(String userName, String password) {
        // 查找用户
        Optional<MUser> userOpt = userRepository.findByUserName(userName);
        if (userOpt.isEmpty()) {
            // 尝试使用邮箱登录
            userOpt = userRepository.findByValidationEmail(userName);
        }

        if (userOpt.isEmpty()) {
            return Optional.empty();
        }

        MUser user = userOpt.get();

        // 验证密码
        if (passwordEncoder.matches(password, user.getPassword())) {
            return Optional.of(user);
        }

        // 向后兼容：尝试使用旧的MD5加密验证
        if (legacyPasswordEncoderUtil != null) {
            String legacyEncodedPassword = legacyPasswordEncoderUtil.encode(password);
            if (legacyEncodedPassword.equals(user.getPassword())) {
                // 密码匹配，更新为新的加密方式
                user.setPassword(passwordEncoder.encode(password));
                userRepository.save(user);
                return Optional.of(user);
            }
        }

        return Optional.empty();
    }

    /**
     * 更新用户签名
     */
    public void updateSignature(MUser user, String content) {
        if (user == null || StringUtils.isBlank(content)) {
            throw new IllegalArgumentException("用户或签名内容不能为空");
        }

        Signature signature = new Signature();
        signature.setContent(content);
        signature.setUpdatedAt(LocalDateTime.now());

        user.setSignature(signature);
        userRepository.save(user);
    }

    /**
     * 关注用户
     */
    public boolean follow(MUser follower, String targetUserName) {
        if (follower == null || StringUtils.isBlank(targetUserName)) {
            return false;
        }

        // 不能关注自己
        if (targetUserName.equals(follower.getUserName())) {
            return false;
        }

        // 查找目标用户
        Optional<MUser> targetOpt = userRepository.findByUserName(targetUserName);
        if (targetOpt.isEmpty()) {
            return false;
        }

        MUser target = targetOpt.get();

        // 检查是否已经关注
        if (follower.isFollowing(target)) {
            return false;
        }

        // 添加关注关系
        boolean followSuccess = follower.addFollow(target);
        boolean fanSuccess = target.addFan(follower);

        if (followSuccess && fanSuccess) {
            userRepository.save(follower);
            userRepository.save(target);
            return true;
        }

        return false;
    }

    /**
     * 取消关注
     */
    public boolean unfollow(MUser follower, String targetUserName) {
        if (follower == null || StringUtils.isBlank(targetUserName)) {
            return false;
        }

        // 查找目标用户
        Optional<MUser> targetOpt = userRepository.findByUserName(targetUserName);
        if (targetOpt.isEmpty()) {
            return false;
        }

        MUser target = targetOpt.get();

        // 检查是否已经关注
        if (!follower.isFollowing(target)) {
            return false;
        }

        // 移除关注关系
        boolean unfollowSuccess = follower.removeFollow(target);
        boolean removeFanSuccess = target.removeFan(follower);

        if (unfollowSuccess && removeFanSuccess) {
            userRepository.save(follower);
            userRepository.save(target);
            return true;
        }

        return false;
    }

    /**
     * 删除用户
     */
    public void delete(MUser user, String password) {
        if (user == null || StringUtils.isBlank(password)) {
            throw new IllegalArgumentException("用户或密码不能为空");
        }

        // 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new SecurityException("密码错误");
        }

        // 删除用户
        userRepository.delete(user);

        // 从其他用户的关注/粉丝列表中移除该用户
        removeUserFromSocialGraph(user);
    }

    /**
     * 从社交关系图中移除用户
     */
    private void removeUserFromSocialGraph(MUser user) {
        // 从关注该用户的用户的粉丝列表中移除
        List<MUser> fans = userRepository.findUsersFollowedByUser(user.getId());
        for (MUser fan : fans) {
            fan.removeFan(user);
            userRepository.save(fan);
        }

        // 从该用户关注的用户的关注列表中移除
        List<MUser> followings = userRepository.findUsersFollowingUser(user.getId());
        for (MUser following : followings) {
            following.removeFollow(user);
            userRepository.save(following);
        }
    }

    /**
     * 根据用户名查找用户
     */
    public Optional<MUser> findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    /**
     * 根据昵称查找用户
     */
    public Optional<MUser> findByNickName(String nickName) {
        return userRepository.findByNickName(nickName);
    }

    /**
     * 根据邮箱查找用户
     */
    public Optional<MUser> findByEmail(String email) {
        return userRepository.findByValidationEmail(email);
    }

    /**
     * 更新用户信息
     */
    public MUser update(MUser user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }

        // 确保用户存在
        if (!userRepository.existsById(user.getId())) {
            throw new IllegalArgumentException("用户不存在");
        }

        return userRepository.save(user);
    }

    /**
     * 获取用户的关注列表
     */
    public Set<MUser> getFollows(MUser user) {
        if (user == null) {
            return new HashSet<>();
        }

        // 重新加载用户以获取完整的关注列表
        Optional<MUser> reloadedUser = userRepository.findById(user.getId());
        return reloadedUser.map(MUser::getFollows).orElse(new HashSet<>());
    }

    /**
     * 获取用户的粉丝列表
     */
    public Set<MUser> getFans(MUser user) {
        if (user == null) {
            return new HashSet<>();
        }

        // 重新加载用户以获取完整的粉丝列表
        Optional<MUser> reloadedUser = userRepository.findById(user.getId());
        return reloadedUser.map(MUser::getFans).orElse(new HashSet<>());
    }

    /**
     * 检查用户名是否可用
     */
    public boolean isUserNameAvailable(String userName) {
        return !userRepository.existsByUserName(userName);
    }

    /**
     * 检查昵称是否可用
     */
    public boolean isNickNameAvailable(String nickName) {
        return !userRepository.existsByNickName(nickName);
    }

    /**
     * 检查邮箱是否可用
     */
    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByValidationEmail(email);
    }

    /**
     * 搜索用户
     */
    public List<MUser> searchUsers(String keyword) {
        if (StringUtils.isBlank(keyword)) {
            return userRepository.findAll();
        }

        // 搜索用户名和昵称包含关键词的用户
        List<MUser> byUserName = userRepository.findByUserNameContaining(keyword);
        List<MUser> byNickName = userRepository.findByNickNameContaining(keyword);

        // 合并结果，去重
        Set<MUser> result = new HashSet<>(byUserName);
        result.addAll(byNickName);

        return List.copyOf(result);
    }

    /**
     * 统计用户数量
     */
    public long countUsers() {
        return userRepository.count();
    }

    /**
     * 增加用户微博数量
     */
    public void incrementTwiSize(MUser user) {
        if (user != null) {
            user.incrementTwiSize();
            userRepository.save(user);
        }
    }

    /**
     * 减少用户微博数量
     */
    public void decrementTwiSize(MUser user) {
        if (user != null) {
            user.decrementTwiSize();
            userRepository.save(user);
        }
    }

    /**
     * 根据ID查找用户
     */
    public Optional<MUser> findById(String id) {
        return userRepository.findById(id);
    }
}