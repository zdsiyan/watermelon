package com.github.watermelon.module.common.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

/**
 * 签名实体类（嵌入式文档）
 * 使用Spring Data MongoDB注解
 */
@Getter
@Setter
public class Signature {

    /**
     * 签名内容
     */
    @Field("content")
    private String content;

    /**
     * 更新时间戳
     */
    @Field("timestamp")
    private LocalDateTime updatedAt;

    /**
     * 创建签名实例
     */
    public static Signature create(String content) {
        Signature signature = new Signature();
        signature.setContent(content);
        signature.setUpdatedAt(LocalDateTime.now());
        return signature;
    }

    /**
     * 更新签名内容
     */
    public void updateContent(String newContent) {
        this.content = newContent;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 获取时间戳（毫秒）
     * 用于向后兼容
     */
    public Long getTimestamp() {
        return updatedAt != null ? updatedAt.toEpochSecond(java.time.ZoneOffset.UTC) * 1000 : null;
    }

    /**
     * 设置时间戳（毫秒）
     * 用于向后兼容
     */
    public void setTimestamp(Long timestamp) {
        if (timestamp != null) {
            this.updatedAt = LocalDateTime.ofEpochSecond(timestamp / 1000, 0, java.time.ZoneOffset.UTC);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Signature signature = (Signature) o;

        if (content != null ? !content.equals(signature.content) : signature.content != null) return false;
        return updatedAt != null ? updatedAt.equals(signature.updatedAt) : signature.updatedAt == null;
    }

    @Override
    public int hashCode() {
        int result = content != null ? content.hashCode() : 0;
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Signature{" +
                "content='" + content + '\'' +
                ", updatedAt=" + updatedAt +
                '}';
    }
}