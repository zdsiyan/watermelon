package com.mgs.watermelon.entity;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;

/**
 * 随便起的名字, 相当于微博
 * @author bruce
 */
@Entity("twibo")
@JsonSerialize(include=JsonSerialize.Inclusion.NON_EMPTY)
public class Twibo extends BaseEntity {
	
	
	//发布的内容
	private String content;
	//时间戳
	private Long timestamp;	
	//标识 0:删除 1:正常 2:隐藏只有自己可见 3:群组可见 
	private Integer status=0;
	//发布的用户
	@Reference
	private MUser user;	
	//转发的原始微博,可为空
	@Reference
	private Twibo original;
	@Embedded
	@JsonIgnore
	private List<Twicomment> twicomments;
	
	public List<Twicomment> getTwicomments() {
		return twicomments;
	}
	public void setTwiComments(List<Twicomment> twicomments) {
		this.twicomments = twicomments;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public MUser getUser() {
		return user;
	}
	public void setUser(MUser user) {
		this.user = user;
	}
	public Twibo getOriginal() {
		return original;
	}
	public void setOriginal(Twibo original) {
		this.original = original;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

}
