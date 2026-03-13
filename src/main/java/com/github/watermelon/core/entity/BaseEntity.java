package com.github.watermelon.core.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础实体类
 * 使用Spring Data MongoDB注解
 */
@Getter
@Setter
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     * Spring Data MongoDB默认使用String类型的ID
     */
    @Id
    @JSONField(serialize = false)
    private String id;

    /**
     * 创建时间
     */
    @CreatedDate
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;

    /**
     * 版本号，用于乐观锁
     */
    @Version
    private Long version;

    /**
     * 获取MongoDB ObjectId格式的ID
     * 用于向后兼容
     */
    @JSONField(name = "_id")
    public String getMongoId() {
        return id;
    }

    /**
     * 判断实体是否为新实体（未保存到数据库）
     */
    public boolean isNew() {
        return id == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseEntity that = (BaseEntity) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id='" + id + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}