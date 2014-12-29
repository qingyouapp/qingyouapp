package com.xwq.qingyouapp.bean;

import java.util.HashSet;
import java.util.Set;


public class UniversityTypes implements java.io.Serializable {

	// Fields

	private Short universityTypeId;
	private String universityType;
	private Set universityLists = new HashSet(0);

	// Constructors

	/** default constructor */
	public UniversityTypes() {
	}

	/** minimal constructor */
	public UniversityTypes(Short universityTypeId, String universityType) {
		this.universityTypeId = universityTypeId;
		this.universityType = universityType;
	}

	/** full constructor */
	public UniversityTypes(Short universityTypeId, String universityType,
			Set universityLists) {
		this.universityTypeId = universityTypeId;
		this.universityType = universityType;
		this.universityLists = universityLists;
	}

	// Property accessors

	public Short getUniversityTypeId() {
		return this.universityTypeId;
	}

	public void setUniversityTypeId(Short universityTypeId) {
		this.universityTypeId = universityTypeId;
	}

	public String getUniversityType() {
		return this.universityType;
	}

	public void setUniversityType(String universityType) {
		this.universityType = universityType;
	}

	public Set getUniversityLists() {
		return this.universityLists;
	}

	public void setUniversityLists(Set universityLists) {
		this.universityLists = universityLists;
	}

}