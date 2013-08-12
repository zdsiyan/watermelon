package com.mgs.watermelon.service;

import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;
import com.google.code.morphia.query.UpdateResults;
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
		MUser result=null;
		if(StringUtils.isNotBlank(param.getUserName())){
			result=findOne("userName",param.getUserName());
		}else if(StringUtils.isNotBlank(param.getValidationEmail())){
			result=findOne("validationEmail", param.getValidationEmail());
		}
		//查询并验证密码
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
	 * @param param
	 * @return
	 */
	public MUser register(MUser param) {
		Query<MUser> query = createQuery();
		query.or(query.criteria("userName").equal(param.getUserName()), 
				query.criteria("nickName").equal(param.getNickName()),
				query.criteria("validationEmail").equal(param.getValidationEmail()));
		MUser result=findOne(query);
		//是否有重名
		if(result!=null){
			return null;
		}
		
		result = new MUser();
		result.setUserName(param.getUserName());
		result.setPassword(passwordEncoderUtil.encodePassword(param.getPassword()));
		result.setNickName(param.getNickName());
		result.setValidationEmail(param.getValidationEmail());
		save(result);
		return result;
	}

	public void signature(MUser user, String content) {
		Query<MUser> query = createQuery().filter("userName", user.getUserName());
		
		Signature signature = new Signature();
		signature.setContent(content);
		signature.setTimestamp(new Date().getTime());
		
		UpdateOperations<MUser> uo = baseDao.createUpdateOperations().set("signature", signature);
		
		UpdateResults<MUser> results= baseDao.update(query, uo);
		if(results!=null && results.getUpdatedCount()==1){
			user.setSignature(signature);
		}
	}

	public boolean follow(MUser me, String name) {
		MUser followUser = findOne("userName", name);

		Set<MUser> meFos = me.getFollows();
		if(meFos.contains(followUser)==false){
			meFos.add(followUser);
		}
		Set<MUser> foFans= followUser.getFans();
		if(foFans.contains(me)==false){
			foFans.add(me);
		}		
		Query<MUser> query = createQuery().filter("userName", me.getUserName());
		UpdateOperations<MUser> uo = baseDao.createUpdateOperations().set("follows", meFos);
		baseDao.update(query, uo);
		
		query=createQuery().filter("userName", followUser.getUserName());
		uo = baseDao.createUpdateOperations().set("fans", foFans);
		baseDao.update(query, uo);
		return true;
	}

	public boolean unfollow(MUser me, String name) {
		MUser followUser = findOne("userName", name);

		Set<MUser> meFos = me.getFollows();
		if(meFos.contains(followUser)==true){
			meFos.remove(followUser);
		}
		Set<MUser> foFans= followUser.getFans();
		if(foFans.contains(me)==true){
			foFans.remove(me);
		}
		
		Query<MUser> query = createQuery().filter("userName", me.getUserName());
		UpdateOperations<MUser> uo = baseDao.createUpdateOperations().set("follows", meFos);
		baseDao.update(query, uo);
		
		query=createQuery().filter("userName", followUser.getUserName());
		uo = baseDao.createUpdateOperations().set("fans", foFans);
		baseDao.update(query, uo);
		return true;
	}
	
}
