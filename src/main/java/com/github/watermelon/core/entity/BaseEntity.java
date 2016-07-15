package com.github.watermelon.core.entity;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;

import com.alibaba.fastjson.annotation.JSONField;

public abstract class BaseEntity {
	@Id
	@JSONField(serialize=false)
	private ObjectId oid;
	
	public ObjectId getOid() {
		return oid;
	}

	public void setOid(ObjectId oid) {
		this.oid = oid;
	}
	
	public String get_id(){
		return oid.toStringMongod();
	}
	
}
