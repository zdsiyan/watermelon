package com.mgs.watermelon.entity;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Reference;

/**
 * 相当于评论
 * @author bruce
 */
@Entity("twicomment")
@JsonSerialize(include=JsonSerialize.Inclusion.NON_EMPTY)
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
