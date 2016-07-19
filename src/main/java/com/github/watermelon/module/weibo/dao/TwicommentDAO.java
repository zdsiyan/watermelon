package com.github.watermelon.module.weibo.dao;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.github.watermelon.module.weibo.entity.Twicomment;

@Repository
public class TwicommentDAO extends BasicDAO<Twicomment, ObjectId>{

	@Autowired
	protected TwicommentDAO(Datastore datastore) {
		super(datastore);
	}
}
