package com.github.watermelon.module.common.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import com.github.watermelon.core.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

/**
 * 用户实体类
 * 使用Spring Data MongoDB注解
 *
 * @author bruce
 */
@Getter
@Setter
@Document(collection = "muser")
public class MUser extends BaseEntity {

    /**
     * 序列化, 为Set做处理
     */
    private static final long serialVersionUID = -4000181846290366322L;

    // 用户名
    @Indexed(unique = true)
    private String userName;

    // 昵称
    @Indexed(unique = true)
    private String nickName;

    // 验证邮箱
    @Indexed(unique = true)
    private String validationEmail;

    // 密码
    @JSONField(serialize = false)
    private String password;

    // 引用 关注人
    @DBRef(lazy = true)
    @JSONField(serialize = false)
    private Set<MUser> follows = new HashSet<>();

    // 引用 被关注人
    @DBRef(lazy = true)
    @JSONField(serialize = false)
    private Set<MUser> fans = new HashSet<>();

    // 子文档 签名
    @JSONField(serialize = false)
    private Signature signature;

    // 微博数量
    private Integer twiSize = 0;

    /**
     * 获取关注数量
     */
    public Integer getFollowsSize() {
        return follows != null ? follows.size() : 0;
    }

    /**
     * 获取粉丝数量
     */
    public Integer getFansSize() {
        return fans != null ? fans.size() : 0;
    }

    /**
     * 获取签名内容
     */
    public String getSignatureContent() {
        return signature != null ? signature.getContent() : null;
    }

    /**
     * 获取微博数量（处理null值）
     */
    public Integer getTwiSize() {
        return twiSize != null ? twiSize : 0;
    }

    /**
     * 添加关注
     */
    public boolean addFollow(MUser user) {
        if (user == null || user.getId() == null || user.getId().equals(this.getId())) {
            return false;
        }
        return follows.add(user);
    }

    /**
     * 移除关注
     */
    public boolean removeFollow(MUser user) {
        if (user == null || user.getId() == null) {
            return false;
        }
        return follows.removeIf(u -> u.getId().equals(user.getId()));
    }

    /**
     * 添加粉丝
     */
    public boolean addFan(MUser user) {
        if (user == null || user.getId() == null || user.getId().equals(this.getId())) {
            return false;
        }
        return fans.add(user);
    }

    /**
     * 移除粉丝
     */
    public boolean removeFan(MUser user) {
        if (user == null || user.getId() == null) {
            return false;
        }
        return fans.removeIf(u -> u.getId().equals(user.getId()));
    }

    /**
     * 检查是否关注了指定用户
     */
    public boolean isFollowing(MUser user) {
        if (user == null || user.getId() == null || follows == null) {
            return false;
        }
        return follows.stream().anyMatch(u -> u.getId().equals(user.getId()));
    }

    /**
     * 检查是否被指定用户关注
     */
    public boolean isFollowedBy(MUser user) {
        if (user == null || user.getId() == null || fans == null) {
            return false;
        }
        return fans.stream().anyMatch(u -> u.getId().equals(user.getId()));
    }

    /**
     * 增加微博数量
     */
    public void incrementTwiSize() {
        this.twiSize = getTwiSize() + 1;
    }

    /**
     * 减少微博数量
     */
    public void decrementTwiSize() {
        int currentSize = getTwiSize();
        if (currentSize > 0) {
            this.twiSize = currentSize - 1;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;

        MUser other = (MUser) obj;

        if (userName != null ? !userName.equals(other.userName) : other.userName != null) return false;
        if (nickName != null ? !nickName.equals(other.nickName) : other.nickName != null) return false;
        if (validationEmail != null ? !validationEmail.equals(other.validationEmail) : other.validationEmail != null)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (nickName != null ? nickName.hashCode() : 0);
        result = 31 * result + (validationEmail != null ? validationEmail.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MUser{" +
                "id='" + getId() + '\'' +
                ", userName='" + userName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", validationEmail='" + validationEmail + '\'' +
                ", followsSize=" + getFollowsSize() +
                ", fansSize=" + getFansSize() +
                ", twiSize=" + getTwiSize() +
                '}';
    }
}