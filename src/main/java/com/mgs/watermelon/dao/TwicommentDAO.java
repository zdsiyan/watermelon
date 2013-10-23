package com.mgs.watermelon.dao;

import org.bson.types.ObjectId;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.dao.BasicDAO;
import com.mgs.watermelon.entity.Twicomment;

public class TwicommentDAO extends BasicDAO<Twicomment, ObjectId>{

	protected TwicommentDAO(Datastore ds) {
		super(ds);
	}
}
