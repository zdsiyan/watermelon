package com.mgs.watermelon.service;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.QueryResults;
import com.mgs.watermelon.dao.TwiboDAO;
import com.mgs.watermelon.entity.MUser;
import com.mgs.watermelon.entity.Twibo;

@Service
public class TwiboService extends MongoBaseService<Twibo, ObjectId> {

	@Autowired
	private TwiboDAO twiboDAO;

	public TwiboDAO getTwiboDAO() {
		return twiboDAO;
	}

	public void setTwiboDAO(TwiboDAO twiboDAO) {
		this.twiboDAO = twiboDAO;
		super.setBaseDao(twiboDAO);
	}

	/**
	 * 简单分页
	 * @param user
	 * @param offset
	 * @param length
	 * @return
	 */
	public List<Twibo> getList(MUser user, Integer offset, Integer length) {
		List<MUser> lists = new ArrayList<MUser>();
		lists.add(user);
		for(MUser u : user.getFollows()){
			lists.add(u);
		}
		Query<Twibo> query = baseDao.createQuery();
		query.filter("user in ", lists).offset(offset).limit(length);
		QueryResults<Twibo> result = baseDao.find(query);
		return result.asList();
	}
	
	
	
}
