package com.github.watermelon.module.weibo.entity;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Reference;

import com.github.watermelon.core.entity.BaseEntity;
import com.github.watermelon.module.common.entity.MUser;

/**
 * 相当于评论
 * @author bruce
 */
@Entity("twicomment")
public class Twicomment extends BaseEntity{
	@Reference
	private Twibo twibo;
	
	private String content;	
	private Long timestamp;
	// 发表评论的用户
	@Reference
	private MUser user;	
	
	public Twibo getTwibo() {
		return twibo;
	}
	public void setTwibo(Twibo twibo) {
		this.twibo = twibo;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	
	public MUser getUser() {
		return user;
	}
	public void setUser(MUser user) {
		this.user = user;
	}
}
