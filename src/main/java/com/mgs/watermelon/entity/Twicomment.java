package com.mgs.watermelon.entity;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Reference;

/**
 * 随便起的名字, 相当于评论
 * @author bruce
 */
@Embedded
public class Twicomment {
	
	private String content;	
	private Long timestamp;
	// 发表评论的用户
	@Reference
	private MUser user;	
	
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
