package com.github.watermelon.module.weibo.service;

import com.github.watermelon.module.common.entity.MUser;
import com.github.watermelon.module.common.repository.MUserRepository;
import com.github.watermelon.module.weibo.entity.Twibo;
import com.github.watermelon.module.weibo.entity.Twicomment;
import com.github.watermelon.module.weibo.repository.TwiboRepository;
import com.github.watermelon.module.weibo.repository.TwicommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 微博服务 - 使用Spring Data MongoDB Repository
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TwiboService {

    private final TwiboRepository twiboRepository;
    private final TwicommentRepository twicommentRepository;
    private final MUserRepository mUserRepository;

    /**
     * 发表微博
     */
    @Transactional
    public Twibo postTwibo(String content, MUser user) {
        Twibo twibo = new Twibo();
        twibo.setContent(content);
        twibo.setTimestamp(System.currentTimeMillis());
        twibo.setUser(user);
        twibo.setPublishedAt(LocalDateTime.now());
        twibo.setStatus(1); // 正常状态

        Twibo savedTwibo = twiboRepository.save(twibo);

        // 更新用户的微博计数
        user.setTwiSize(user.getTwiSize() + 1);
        mUserRepository.save(user);

        return savedTwibo;
    }

    /**
     * 获取用户时间线（关注的人的微博）
     */
    public Page<Twibo> getTimeline(MUser user, int page, int size) {
        // 获取用户关注的人ID列表
        List<String> followedUserIds = new ArrayList<>();
        followedUserIds.add(user.getId()); // 包括自己的微博

        if (user.getFollows() != null) {
            user.getFollows().forEach(followedUser -> {
                followedUserIds.add(followedUser.getId());
            });
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publishedAt"));
        return twiboRepository.findTimelineByUserIds(followedUserIds, pageable);
    }

    /**
     * 获取微博列表（简单分页）
     */
    public List<Twibo> getList(MUser user, Integer offset, Integer length) {
        // 获取用户关注的人ID列表
        List<String> userIds = new ArrayList<>();
        userIds.add(user.getId());

        if (user.getFollows() != null) {
            user.getFollows().forEach(followedUser -> {
                userIds.add(followedUser.getId());
            });
        }

        Pageable pageable = PageRequest.of(offset / length, length, Sort.by(Sort.Direction.DESC, "timestamp"));
        Page<Twibo> page = twiboRepository.findTimelineByUserIds(userIds, pageable);
        return page.getContent();
    }

    /**
     * 发表评论
     */
    @Transactional
    public Twicomment postComment(String twiboId, MUser user, String content) {
        Optional<Twibo> twiboOpt = twiboRepository.findById(twiboId);
        if (twiboOpt.isEmpty()) {
            throw new IllegalArgumentException("微博不存在: " + twiboId);
        }

        Twibo twibo = twiboOpt.get();

        // 保存评论
        Twicomment comment = new Twicomment();
        comment.setTwibo(twibo);
        comment.setUser(user);
        comment.setContent(content);
        comment.setTimestamp(System.currentTimeMillis());

        Twicomment savedComment = twicommentRepository.save(comment);

        // 更新微博的评论计数
        twibo.setCommentSize(twibo.getCommentSize() + 1);
        twiboRepository.save(twibo);

        return savedComment;
    }

    /**
     * 获取微博的评论
     */
    public List<Twicomment> getComments(String twiboId, Integer offset, Integer length) {
        Pageable pageable = PageRequest.of(offset / length, length, Sort.by(Sort.Direction.DESC, "timestamp"));
        Page<Twicomment> page = twicommentRepository.findByTwiboId(twiboId, pageable);
        return page.getContent();
    }

    /**
     * 根据ID获取微博
     */
    public Optional<Twibo> getTwiboById(String id) {
        return twiboRepository.findById(id);
    }

    /**
     * 删除微博
     */
    @Transactional
    public void deleteTwibo(String id, MUser user) {
        Optional<Twibo> twiboOpt = twiboRepository.findById(id);
        if (twiboOpt.isPresent()) {
            Twibo twibo = twiboOpt.get();
            // 检查权限：只有微博作者可以删除
            if (twibo.getUser().getId().equals(user.getId())) {
                twibo.setStatus(0); // 标记为删除状态
                twiboRepository.save(twibo);

                // 更新用户的微博计数
                user.setTwiSize(Math.max(0, user.getTwiSize() - 1));
                mUserRepository.save(user);
            } else {
                throw new SecurityException("无权删除此微博");
            }
        }
    }

    /**
     * 获取热门微博
     */
    public Page<Twibo> getHotTwibos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "commentSize", "likeCount"));
        return twiboRepository.findHotTwibos(pageable);
    }

    /**
     * 搜索微博
     */
    public Page<Twibo> searchTwibos(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publishedAt"));
        return twiboRepository.findByContentContaining(keyword, pageable);
    }
}