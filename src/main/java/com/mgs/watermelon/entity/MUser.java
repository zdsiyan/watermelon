package com.mgs.watermelon.entity;

import java.util.Set;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;

/**
 * @author bruce
 */
@Entity("muser")
@JsonSerialize(include=JsonSerialize.Inclusion.NON_EMPTY)
public class MUser extends BaseEntity{
	//用户名
	private String userName;
	//昵称
	private String nickName;
	//验证邮箱
	private String validationEmail;
	//密码
	private String password;
	
	//引用 关注人
	@Reference
	private Set<MUser> follows;
	//引用 被关注人
	@Reference
	private Set<MUser> fans;
	//子文档 签名
	@Embedded
	private Signature signature;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getValidationEmail() {
		return validationEmail;
	}

	public void setValidationEmail(String validationEmail) {
		this.validationEmail = validationEmail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<MUser> getFollows() {
		return follows;
	}

	public void setFollows(Set<MUser> follows) {
		this.follows = follows;
	}

	public Set<MUser> getFans() {
		return fans;
	}

	public void setFans(Set<MUser> fans) {
		this.fans = fans;
	}

	public Signature getSignature() {
		return signature;
	}

	public void setSignature(Signature signature) {
		this.signature = signature;
	}

}
