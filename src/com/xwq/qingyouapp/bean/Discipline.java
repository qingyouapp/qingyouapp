package com.xwq.qingyouapp.bean;

public class Discipline {
	private short id;
	private String name;

	public Discipline() {
		// TODO Auto-generated constructor stub
	}

	public Discipline(short id, String name) {
		this.id = id;
		this.name = name;
	}

	public short getId() {
		return id;
	}

	public void setId(short id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
