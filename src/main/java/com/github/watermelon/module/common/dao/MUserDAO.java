package com.github.watermelon.module.common.dao;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.github.watermelon.module.common.entity.MUser;

@Repository
public class MUserDAO extends BasicDAO<MUser, ObjectId> {

	@Autowired
	protected MUserDAO(Datastore datastore) {
		super(datastore);
	}

}
