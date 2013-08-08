package com.mgs.watermelon.service;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mgs.watermelon.dao.MUserDAO;
import com.mgs.watermelon.entity.MUser;

@Service
public class MUserService extends MongoBaseService<MUser, ObjectId>{
	@Autowired
	private MUserDAO mUserDAO;

	public MUserDAO getmUserDAO() {
		return mUserDAO;
	}

	public void setmUserDAO(MUserDAO mUserDAO) {
		this.mUserDAO = mUserDAO;
		super.setBaseDao(mUserDAO);
	}
	
}
