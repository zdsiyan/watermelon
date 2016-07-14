package com.github.watermelon.dao;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

import com.github.watermelon.entity.Twicomment;

public class TwicommentDAO extends BasicDAO<Twicomment, ObjectId>{

	protected TwicommentDAO(Datastore ds) {
		super(ds);
	}
}
