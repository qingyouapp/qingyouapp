package com.xwq.qingyouapp.bean;

public class FriendStandards {

	private Short sex;
	private HeightBT height;
	private WeightBT weight;
	private AgeBT age;
	private GradeBT grade;
	private String tags;
	private Integer currentCity;

	public FriendStandards(Short sex, HeightBT height, WeightBT weight, AgeBT age, GradeBT grade,
			String tags, Integer currentCity) {
		super();
		this.sex = sex;
		this.height = height;
		this.weight = weight;
		this.age = age;
		this.grade = grade;
		this.tags = tags;
		this.currentCity = currentCity;
	}

	public Short getSex() {
		return sex;
	}

	public void setSex(Short sex) {
		this.sex = sex;
	}

	public HeightBT getHeight() {
		return height;
	}

	public void setHeight(HeightBT height) {
		this.height = height;
	}

	public WeightBT getWeight() {
		return weight;
	}

	public void setWeight(WeightBT weight) {
		this.weight = weight;
	}

	public AgeBT getAge() {
		return age;
	}

	public void setAge(AgeBT age) {
		this.age = age;
	}

	public GradeBT getGrade() {
		return grade;
	}

	public void setGrade(GradeBT grade) {
		this.grade = grade;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public Integer getCurrentCity() {
		return currentCity;
	}

	public void setCurrentCity(Integer currentCity) {
		this.currentCity = currentCity;
	}

}
