package com.mgs.watermelon.dao;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

import com.mgs.watermelon.entity.Twibo;

public class TwiboDAO extends BasicDAO<Twibo, ObjectId>{

	protected TwiboDAO(Datastore ds) {
		super(ds);
	}
}
