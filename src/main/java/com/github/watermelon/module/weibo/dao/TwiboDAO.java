package com.github.watermelon.module.weibo.dao;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.github.watermelon.module.weibo.entity.Twibo;

@Repository
public class TwiboDAO extends BasicDAO<Twibo, ObjectId>{

	@Autowired
	protected TwiboDAO(Datastore datastore) {
		super(datastore);
	}
}
