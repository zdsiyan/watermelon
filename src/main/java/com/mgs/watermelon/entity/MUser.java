package com.mgs.watermelon.entity;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.google.code.morphia.annotations.Entity;

@Entity
@JsonSerialize(include=JsonSerialize.Inclusion.NON_EMPTY)
public class MUser extends BaseEntity{


	private String userName;
	private String nickName;
	private String validationEmail;
	private String password;

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

}
