package com.mgs.watermelon.service;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.code.morphia.query.Query;
import com.mgs.watermelon.dao.MUserDAO;
import com.mgs.watermelon.entity.MUser;
import com.mgs.watermelon.entity.Signature;
import com.mgs.watermelon.util.PasswordEncoderUtil;

@Service
public class MUserService extends MongoBaseService<MUser, ObjectId>{
	@Autowired
	private MUserDAO mUserDAO;
	
	@Autowired
	private PasswordEncoderUtil passwordEncoderUtil;

	public MUserDAO getmUserDAO() {
		return mUserDAO;
	}

	public void setmUserDAO(MUserDAO mUserDAO) {
		this.mUserDAO = mUserDAO;
		super.setBaseDao(mUserDAO);
	}

	public PasswordEncoderUtil getPasswordEncoderUtil() {
		return passwordEncoderUtil;
	}

	public void setPasswordEncoderUtil(PasswordEncoderUtil passwordEncoderUtil) {
		this.passwordEncoderUtil = passwordEncoderUtil;
	}

	/**
	 * 登录, 需要userName或validationEmail
	 * @param param
	 * @return
	 */
	public MUser login(MUser param) {
		Query<MUser> query = null;
		if(StringUtils.isNotBlank(param.getUserName())){
			query=createQuery().filter("userName",param.getUserName());
		}else if(StringUtils.isNotBlank(param.getValidationEmail())){
			query=createQuery().filter("validationEmail",param.getValidationEmail());
		}else{
			return null;
		}
		//查询并验证密码
		MUser result=findOne(query);
		if(result!=null && passwordEncoderUtil.isPasswordValid(result.getPassword(), param.getPassword())){
			return result;
		}
		return null;
	}

	/**
	 * 删除
	 * @param result
	 * @param password
	 */
	public void delete(MUser result, String password) {
		//删除前验证密码
		if(result!=null && passwordEncoderUtil.isPasswordValid(result.getPassword(), password)){
			delete(result);
		}
	}

	/**
	 * 注册前检查
	 * @param username
	 * @param password
	 * @param nickname
	 * @param email
	 * @return
	 */
	public MUser register(String username, String password, String nickname,
			String email) {
		Query<MUser> query = createQuery();
		query.or(query.criteria("userName").equal(username), 
				query.criteria("nickName").equal(nickname),
				query.criteria("validationEmail").equal(email));
		MUser result=findOne(query);
		//是否有重名
		if(result!=null){
			return null;
		}
		
		result = new MUser();
		result.setUserName(username);
		result.setPassword(passwordEncoderUtil.encodePassword(password));
		result.setNickName(nickname);
		result.setValidationEmail(email);
		save(result);
		return result;
	}

	public MUser signature(MUser user, String content) {
		Query<MUser> query = createQuery().filter("userName", user.getUserName());
		MUser result = findOne(query);
		if(result==null){
			return null;
		}
		Signature signature = new Signature();
		signature.setContent(content);
		signature.setTimestamp(new Date().getTime());
		result.setSignature(signature);
		save(result);
		return result;
	}
	
}
