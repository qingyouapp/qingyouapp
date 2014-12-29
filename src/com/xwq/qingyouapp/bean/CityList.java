package com.xwq.qingyouapp.bean;
import java.util.HashSet;
import java.util.Set;

public class CityList implements java.io.Serializable {

	// Fields

	private Short cityId;
	private ProvinceList provinceList;
	private String cityName;
	private Set userMetadatasForHometown = new HashSet(0);
	private Set universityLists = new HashSet(0);
	private Set userMetadatasForCurrentCity = new HashSet(0);

	// Constructors

	/** default constructor */
	public CityList() {
	}

	/** minimal constructor */
	public CityList(Short cityId, ProvinceList provinceList, String cityName) {
		this.cityId = cityId;
		this.provinceList = provinceList;
		this.cityName = cityName;
	}

	/** full constructor */
	public CityList(Short cityId, ProvinceList provinceList, String cityName,
			Set userMetadatasForHometown, Set universityLists,
			Set userMetadatasForCurrentCity) {
		this.cityId = cityId;
		this.provinceList = provinceList;
		this.cityName = cityName;
		this.userMetadatasForHometown = userMetadatasForHometown;
		this.universityLists = universityLists;
		this.userMetadatasForCurrentCity = userMetadatasForCurrentCity;
	}

	// Property accessors

	public Short getCityId() {
		return this.cityId;
	}

	public void setCityId(Short cityId) {
		this.cityId = cityId;
	}

	public ProvinceList getProvinceList() {
		return this.provinceList;
	}

	public void setProvinceList(ProvinceList provinceList) {
		this.provinceList = provinceList;
	}

	public String getCityName() {
		return this.cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Set getUserMetadatasForHometown() {
		return this.userMetadatasForHometown;
	}

	public void setUserMetadatasForHometown(Set userMetadatasForHometown) {
		this.userMetadatasForHometown = userMetadatasForHometown;
	}

	public Set getUniversityLists() {
		return this.universityLists;
	}

	public void setUniversityLists(Set universityLists) {
		this.universityLists = universityLists;
	}

	public Set getUserMetadatasForCurrentCity() {
		return this.userMetadatasForCurrentCity;
	}

	public void setUserMetadatasForCurrentCity(Set userMetadatasForCurrentCity) {
		this.userMetadatasForCurrentCity = userMetadatasForCurrentCity;
	}

}