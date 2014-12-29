package com.xwq.qingyouapp.bean;

import java.util.HashSet;
import java.util.Set;


public class ProvinceList implements java.io.Serializable {

	// Fields

	private Short provinceId;
	private String provinceName;
	private Set cityLists = new HashSet(0);

	// Constructors

	/** default constructor */
	public ProvinceList() {
	}

	/** minimal constructor */
	public ProvinceList(Short provinceId, String provinceName) {
		this.provinceId = provinceId;
		this.provinceName = provinceName;
	}

	/** full constructor */
	public ProvinceList(Short provinceId, String provinceName, Set cityLists) {
		this.provinceId = provinceId;
		this.provinceName = provinceName;
		this.cityLists = cityLists;
	}

	// Property accessors

	public Short getProvinceId() {
		return this.provinceId;
	}

	public void setProvinceId(Short provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvinceName() {
		return this.provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public Set getCityLists() {
		return this.cityLists;
	}

	public void setCityLists(Set cityLists) {
		this.cityLists = cityLists;
	}

}