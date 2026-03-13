package com.github.watermelon.module.weibo.entity;

import com.github.watermelon.core.entity.BaseEntity;
import com.github.watermelon.module.common.entity.MUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * 微博实体类
 * 使用Spring Data MongoDB注解
 *
 * @author bruce
 */
@Getter
@Setter
@Document(collection = "twibo")
public class Twibo extends BaseEntity {

    /**
     * 状态常量
     */
    public static final int STATUS_DELETED = 0;
    public static final int STATUS_NORMAL = 1;
    public static final int STATUS_HIDDEN = 2;
    public static final int STATUS_GROUP_VISIBLE = 3;

    // 发布的内容
    private String content;

    // 发布时间
    private LocalDateTime publishedAt;

    // 标识 0:删除 1:正常 2:隐藏只有自己可见 3:群组可见
    private Integer status = STATUS_NORMAL;

    // 发布的用户
    @DBRef
    private MUser user;

    // 转发的原始微博,可为空
    @DBRef(lazy = true)
    private Twibo original;

    // 评论计数器
    private Integer commentSize = 0;

    /**
     * 创建微博实例
     */
    public static Twibo create(String content, MUser user) {
        Twibo twibo = new Twibo();
        twibo.setContent(content);
        twibo.setUser(user);
        twibo.setPublishedAt(LocalDateTime.now());
        twibo.setStatus(STATUS_NORMAL);
        return twibo;
    }

    /**
     * 创建转发微博
     */
    public static Twibo createRepost(String content, MUser user, Twibo original) {
        Twibo twibo = create(content, user);
        twibo.setOriginal(original);
        return twibo;
    }

    /**
     * 获取时间戳（毫秒）
     * 用于向后兼容
     */
    public Long getTimestamp() {
        return publishedAt != null ? publishedAt.toEpochSecond(java.time.ZoneOffset.UTC) * 1000 : null;
    }

    /**
     * 设置时间戳（毫秒）
     * 用于向后兼容
     */
    public void setTimestamp(Long timestamp) {
        if (timestamp != null) {
            this.publishedAt = LocalDateTime.ofEpochSecond(timestamp / 1000, 0, java.time.ZoneOffset.UTC);
        }
    }

    /**
     * 检查微博是否正常（可见）
     */
    public boolean isNormal() {
        return STATUS_NORMAL == status;
    }

    /**
     * 检查微博是否已删除
     */
    public boolean isDeleted() {
        return STATUS_DELETED == status;
    }

    /**
     * 检查微博是否隐藏
     */
    public boolean isHidden() {
        return STATUS_HIDDEN == status;
    }

    /**
     * 检查微博是否仅群组可见
     */
    public boolean isGroupVisible() {
        return STATUS_GROUP_VISIBLE == status;
    }

    /**
     * 软删除微博
     */
    public void softDelete() {
        this.status = STATUS_DELETED;
    }

    /**
     * 隐藏微博
     */
    public void hide() {
        this.status = STATUS_HIDDEN;
    }

    /**
     * 恢复微博
     */
    public void restore() {
        this.status = STATUS_NORMAL;
    }

    /**
     * 增加评论数量
     */
    public void incrementCommentSize() {
        this.commentSize = getCommentSize() + 1;
    }

    /**
     * 减少评论数量
     */
    public void decrementCommentSize() {
        int currentSize = getCommentSize();
        if (currentSize > 0) {
            this.commentSize = currentSize - 1;
        }
    }

    /**
     * 获取评论数量（处理null值）
     */
    public Integer getCommentSize() {
        return commentSize != null ? commentSize : 0;
    }

    /**
     * 检查是否为转发微博
     */
    public boolean isRepost() {
        return original != null;
    }

    /**
     * 获取原始微博内容（如果是转发）
     */
    public String getOriginalContent() {
        return original != null ? original.getContent() : null;
    }

    /**
     * 获取原始发布用户（如果是转发）
     */
    public MUser getOriginalUser() {
        return original != null ? original.getUser() : null;
    }

    @Override
    public String toString() {
        return "Twibo{" +
                "id='" + getId() + '\'' +
                ", content='" + (content != null ? content.substring(0, Math.min(content.length(), 50)) : "") + "'" +
                ", publishedAt=" + publishedAt +
                ", status=" + status +
                ", userId='" + (user != null ? user.getId() : null) + '\'' +
                ", isRepost=" + isRepost() +
                ", commentSize=" + getCommentSize() +
                '}';
    }
}