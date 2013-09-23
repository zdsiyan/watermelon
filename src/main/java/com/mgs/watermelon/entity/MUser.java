package com.mgs.watermelon.entity;

import java.io.Serializable;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.Reference;
import com.google.code.morphia.utils.IndexDirection;

/**
 * @author bruce
 */
@Entity("muser")
@JsonSerialize(include=JsonSerialize.Inclusion.NON_EMPTY)
public class MUser extends BaseEntity implements Serializable{
	/**
	 * 序列化, 为Set做处理
	 */
	private static final long serialVersionUID = -4000181846290366322L;
	
	//用户名
	@Indexed(value=IndexDirection.ASC, unique=true, dropDups=true)
	private String userName;
	//昵称
	@Indexed(value=IndexDirection.ASC, unique=true, dropDups=true)
	private String nickName;
	//验证邮箱
	@Indexed(value=IndexDirection.ASC, unique=true, dropDups=true)
	private String validationEmail;
	//密码
	@JsonIgnore
	private String password;
	
	//引用 关注人
	@Reference
	@JsonIgnore
	private Set<MUser> follows;
	//引用 被关注人
	@Reference
	@JsonIgnore
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
	
	public Integer getFollowsSize(){
		if(follows!=null)
			return follows.size();
		return 0;
	}
	
	public Integer getFansSize(){
		if(fans!=null)
			return fans.size();
		return 0;
	}
	
	public Signature getSignature() {
		return signature;
	}

	public void setSignature(Signature signature) {
		this.signature = signature;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((nickName == null) ? 0 : nickName.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((signature == null) ? 0 : signature.hashCode());
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
		result = prime * result
				+ ((validationEmail == null) ? 0 : validationEmail.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MUser other = (MUser) obj;
		if (nickName == null) {
			if (other.nickName != null)
				return false;
		} else if (!nickName.equals(other.nickName))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (signature == null) {
			if (other.signature != null)
				return false;
		} else if (!signature.equals(other.signature))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		if (validationEmail == null) {
			if (other.validationEmail != null)
				return false;
		} else if (!validationEmail.equals(other.validationEmail))
			return false;
		return true;
	}

}
