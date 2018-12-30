package com.ec.bussiness.logic;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
// User is a keyword in some SQL dialects!
@Table(name = "Models")
public class ModelSet {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	private String classname;
	@Lob
	private byte[] object;

	private Timestamp date;

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public byte[] getObject() {
		return object;
	}

	public void setObject(byte[] object) {
		this.object = object;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

}
