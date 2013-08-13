package com.mgs.watermelon.dao;

import org.bson.types.ObjectId;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.dao.BasicDAO;
import com.mgs.watermelon.entity.Twibo;

public class TwiboDAO extends BasicDAO<Twibo, ObjectId>{

	protected TwiboDAO(Datastore ds) {
		super(ds);
	}
}
