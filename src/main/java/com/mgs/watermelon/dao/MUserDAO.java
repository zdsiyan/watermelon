package com.mgs.watermelon.dao;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

import com.mgs.watermelon.entity.MUser;

public class MUserDAO extends BasicDAO<MUser, ObjectId> {

	protected MUserDAO(Datastore ds) {
		super(ds);
	}

}
