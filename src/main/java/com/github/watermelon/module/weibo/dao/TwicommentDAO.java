package com.github.watermelon.module.weibo.dao;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

import com.github.watermelon.module.weibo.entity.Twicomment;

public class TwicommentDAO extends BasicDAO<Twicomment, ObjectId>{

	protected TwicommentDAO(Datastore ds) {
		super(ds);
	}
}
