package com.mgs.watermelon.entity;

import com.google.code.morphia.annotations.Embedded;

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
