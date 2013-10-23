package com.mgs.watermelon.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.QueryResults;
import com.google.code.morphia.query.UpdateOperations;
import com.mgs.watermelon.dao.MUserDAO;
import com.mgs.watermelon.dao.TwiboDAO;
import com.mgs.watermelon.dao.TwicommentDAO;
import com.mgs.watermelon.entity.MUser;
import com.mgs.watermelon.entity.Twibo;
import com.mgs.watermelon.entity.Twicomment;

@Service
public class TwiboService extends MongoBaseService<Twibo, ObjectId> {


	@Autowired
	private MUserDAO mUserDAO;

	@Autowired
	private TwiboDAO twiboDAO;
	
	@Autowired
	private TwicommentDAO twicommentDAO;

	public TwiboDAO getTwiboDAO() {
		return twiboDAO;
	}

	public void setTwiboDAO(TwiboDAO twiboDAO) {
		this.twiboDAO = twiboDAO;
		super.setBaseDao(twiboDAO);
	}

	/**
	 * 发表twibo
	 * @param content
	 * @param user
	 * @return
	 */
	public Twibo postTwibo(String content, MUser user) {
		Twibo result = new Twibo();
		result.setContent(content);
		result.setTimestamp(new Date().getTime());
		result.setUser(user);
		baseDao.save(result);
		
		Query<MUser> query = mUserDAO.createQuery().field("_id").equal(user.get_id());
		UpdateOperations<MUser> uo = mUserDAO.createUpdateOperations().inc("twiSize");
		mUserDAO.update(query, uo);
		
		return result;
		
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
		query.filter("user in ", lists).offset(offset).limit(length).order("-timestamp");
		QueryResults<Twibo> result = baseDao.find(query);
		return result.asList();
	}	
	
	/**
	 * 发表评论
	 * @param twibo
	 * @param user
	 * @param content
	 * @return
	 */
	public Twicomment postComment(String tid, MUser user, String content) {
		Twibo twibo = get(new ObjectId(tid));
		//保存评论
		Twicomment comment = new Twicomment();
		comment.setTwibo(twibo);
		comment.setUser(user);
		comment.setContent(content);
		comment.setTimestamp(new Date().getTime());
		twicommentDAO.save(comment);
		
		Query<Twibo> query = twiboDAO.createQuery().field("_id").equal(tid);
		UpdateOperations<Twibo> uo = twiboDAO.createUpdateOperations().inc("commentSize");
		twiboDAO.update(query, uo);
		
		return comment;
	}

	public List<Twicomment> getComments(String tid, Integer offset, Integer length) {
		Twibo twibo=twiboDAO.get(new ObjectId(tid));
		
		Query<Twicomment> query = twicommentDAO.createQuery();
		query.filter("twibo", twibo).offset(offset).limit(length).order("-timestamp");
		QueryResults<Twicomment> result = twicommentDAO.find(query);
		return result.asList();
	}
}
