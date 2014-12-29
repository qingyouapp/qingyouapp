package com.xwq.qingyouapp.bean;

import java.util.HashSet;
import java.util.Set;

public class UniversityList implements java.io.Serializable {

	// Fields

	private Short universityId;
	private UniversityTypes universityTypes;
	private CityList cityList;
	private String universityName;
	private Set userMetadatas = new HashSet(0);

	// Constructors

	/** default constructor */
	public UniversityList() {
	}

	/** minimal constructor */
	public UniversityList(Short universityId, UniversityTypes universityTypes,
			CityList cityList, String universityName) {
		this.universityId = universityId;
		this.universityTypes = universityTypes;
		this.cityList = cityList;
		this.universityName = universityName;
	}

	/** full constructor */
	public UniversityList(Short universityId, UniversityTypes universityTypes,
			CityList cityList, String universityName, Set userMetadatas) {
		this.universityId = universityId;
		this.universityTypes = universityTypes;
		this.cityList = cityList;
		this.universityName = universityName;
		this.userMetadatas = userMetadatas;
	}

	// Property accessors

	public Short getUniversityId() {
		return this.universityId;
	}

	public void setUniversityId(Short universityId) {
		this.universityId = universityId;
	}

	public UniversityTypes getUniversityTypes() {
		return this.universityTypes;
	}

	public void setUniversityTypes(UniversityTypes universityTypes) {
		this.universityTypes = universityTypes;
	}

	public CityList getCityList() {
		return this.cityList;
	}

	public void setCityList(CityList cityList) {
		this.cityList = cityList;
	}

	public String getUniversityName() {
		return this.universityName;
	}

	public void setUniversityName(String universityName) {
		this.universityName = universityName;
	}

	public Set getUserMetadatas() {
		return this.userMetadatas;
	}

	public void setUserMetadatas(Set userMetadatas) {
		this.userMetadatas = userMetadatas;
	}

}