package com.mgs.watermelon.entity;

import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.google.code.morphia.annotations.Id;

public abstract class BaseEntity {
	@Id
	@JsonIgnore
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
