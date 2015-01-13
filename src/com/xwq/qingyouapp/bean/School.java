package com.xwq.qingyouapp.bean;

public class School {
	
	private Integer id;
	private String name;

	public School() {
		// TODO Auto-generated constructor stub
	}
	
	public School(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



}
