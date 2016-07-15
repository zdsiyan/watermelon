package com.github.watermelon.module.common.entity;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class Signature {
	
	private String content;
	private Long timestamp;
	
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
}
