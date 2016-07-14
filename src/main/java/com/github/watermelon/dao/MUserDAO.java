package com.github.watermelon.dao;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

import com.github.watermelon.entity.MUser;

public class MUserDAO extends BasicDAO<MUser, ObjectId> {

	protected MUserDAO(Datastore ds) {
		super(ds);
	}

}
