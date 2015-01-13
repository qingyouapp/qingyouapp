package com.xwq.qingyouapp.bean;

public class UserMetadata implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer userid;
	private Integer hometownCity;
	private Integer currentCity;
	private Integer university;
	private String username;
	private String nickname;
	private String password;
	private String phonenum;
	private String registermail;
	private String email;
	private Short sex;
	private Long birthday;
	private Short height;
	private Short weight;
	private String eduBackground;
	private Short grade;
	private String edueducation;
	private Short major;
	private String interests;
	private String selfValue;
	private String signature;
	private String friendStandards;
	private Short verificationMode;
	private Short constellation;
	private Integer age;
	private String headPortrait;
	private String photoAlbum;
	private String qqNumber;
	private Boolean varificationPass;
	private String verifyCode;
	private Long verifyTime;
	private Long registerDate;

	
	/** default constructor */
	public UserMetadata() {
	}

	// Property accessors

	public UserMetadata(Integer userid, Integer hometownCity, Integer currentCity,
			Integer university, String username, String nickname, String password, String phonenum,
			String registermail, String email, Short sex, Long birthday, Short height,
			Short weight, String eduBackground, Short grade, String edueducation, Short major,
			String interests, String selfValue, String signature, String friendStandards,
			Short verificationMode, Short constellation, Integer age, String headPortrait,
			String photoAlbum, String qqNumber, String verifyCode, Long verifyTime,
			Boolean varificationPass, Long registerDate) {
		super();
		this.userid = userid;
		this.hometownCity = hometownCity;
		this.currentCity = currentCity;
		this.university = university;
		this.username = username;
		this.nickname = nickname;
		this.password = password;
		this.phonenum = phonenum;
		this.registermail = registermail;
		this.email = email;
		this.sex = sex;
		this.birthday = birthday;
		this.height = height;
		this.weight = weight;
		this.eduBackground = eduBackground;
		this.grade = grade;
		this.edueducation = edueducation;
		this.major = major;
		this.interests = interests;
		this.selfValue = selfValue;
		this.signature = signature;
		this.friendStandards = friendStandards;
		this.verificationMode = verificationMode;
		this.constellation = constellation;
		this.age = age;
		this.headPortrait = headPortrait;
		this.photoAlbum = photoAlbum;
		this.qqNumber = qqNumber;
		this.verifyCode = verifyCode;
		this.varificationPass = varificationPass;
		this.registerDate = registerDate;
		this.verifyTime = verifyTime;
	}

	public Integer getUserid() {
		return this.userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public Integer getHometownCity() {
		return hometownCity;
	}

	public void setHometownCity(Integer hometownCity) {
		this.hometownCity = hometownCity;
	}

	public Integer getCurrentCity() {
		return currentCity;
	}

	public void setCurrentCity(Integer currentCity) {
		this.currentCity = currentCity;
	}

	public Integer getUniversity() {
		return university;
	}

	public void setUniversity(Integer university) {
		this.university = university;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return this.nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhonenum() {
		return this.phonenum;
	}

	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}

	public String getRegistermail() {
		return this.registermail;
	}

	public void setRegistermail(String registermail) {
		this.registermail = registermail;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Short getSex() {
		return this.sex;
	}

	public void setSex(Short sex) {
		this.sex = sex;
	}

	public Long getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Long birthday) {
		this.birthday = birthday;
	}

	public Short getHeight() {
		return this.height;
	}

	public void setHeight(Short height) {
		this.height = height;
	}

	public Long getregisterDate() {
		return registerDate;
	}

	public void setregisterDate(Long registerDate) {
		this.registerDate = registerDate;
	}

	public Short getWeight() {
		return this.weight;
	}

	public void setWeight(Short weight) {
		this.weight = weight;
	}

	public String getEduBackground() {
		return this.eduBackground;
	}

	public void setEduBackground(String eduBackground) {
		this.eduBackground = eduBackground;
	}

	public Short getGrade() {
		return this.grade;
	}

	public void setGrade(Short grade) {
		this.grade = grade;
	}

	public String getEdueducation() {
		return this.edueducation;
	}

	public void setEdueducation(String edueducation) {
		this.edueducation = edueducation;
	}

	public Short getMajor() {
		return this.major;
	}

	public void setMajor(Short major) {
		this.major = major;
	}

	public String getInterests() {
		return this.interests;
	}

	public void setInterests(String interests) {
		this.interests = interests;
	}

	public String getSelfValue() {
		return this.selfValue;
	}

	public void setSelfValue(String selfValue) {
		this.selfValue = selfValue;
	}

	public String getSignature() {
		return this.signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getFriendStandards() {
		return this.friendStandards;
	}

	public void setFriendStandards(String friendStandards) {
		this.friendStandards = friendStandards;
	}

	public Short getVerificationMode() {
		return this.verificationMode;
	}

	public void setVerificationMode(Short verificationMode) {
		this.verificationMode = verificationMode;
	}

	public Short getConstellation() {
		return this.constellation;
	}

	public void setConstellation(Short constellation) {
		this.constellation = constellation;
	}

	public Integer getAge() {
		return this.age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getHeadPortrait() {
		return this.headPortrait;
	}

	public void setHeadPortrait(String headPortrait) {
		this.headPortrait = headPortrait;
	}

	public String getPhotoAlbum() {
		return this.photoAlbum;
	}

	public void setPhotoAlbum(String photoAlbum) {
		this.photoAlbum = photoAlbum;
	}

	public String getQqNumber() {
		return this.qqNumber;
	}

	public void setQqNumber(String qqNumber) {
		this.qqNumber = qqNumber;
	}

	public Boolean getVarificationPass() {
		return this.varificationPass;
	}

	public void setVarificationPass(Boolean varificationPass) {
		this.varificationPass = varificationPass;
	}

	public Long getVerifyTime() {
		return verifyTime;
	}

	public void setVerifyTime(Long verifyTime) {
		this.verifyTime = verifyTime;
	}

}