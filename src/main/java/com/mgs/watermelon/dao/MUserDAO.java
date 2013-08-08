package com.mgs.watermelon.dao;

import org.bson.types.ObjectId;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.dao.BasicDAO;
import com.mgs.watermelon.entity.MUser;

public class MUserDAO extends BasicDAO<MUser, ObjectId> {

	protected MUserDAO(Datastore ds) {
		super(ds);
	}

}
