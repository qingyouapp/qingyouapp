package com.xwq.qingyouapp.bean;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * UserMetadata entity. @author MyEclipse Persistence Tools
 */

public class UserMetadata implements java.io.Serializable {

	// Fields

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
	private String sex;
	private Date birthday;
	private Short height;
	private Short weight;
	private String eduBackground;
	private String grade;
	private String edueducation;
	private String major;
	private String interests;
	private String selfValue;
	private String signature;
	private String friendStandards;
	private Short verificationMode;
	private String constellation;
	private Integer age;
	private String headPortrait;
	private String photoAlbum;
	private String qqNumber;
	private Boolean varificationPass;
	private Set registerFriendsesForFriend1Id = new HashSet(0);
	private Set matchTimelyResultsForUserbId = new HashSet(0);
	private Set registerFriendsesForFriend2Id = new HashSet(0);
	private Set registerScreenshots = new HashSet(0);
	private Set registerFriendsesForUserid = new HashSet(0);
	private Set recommendedTimelyResults = new HashSet(0);
	private Set registerEmails = new HashSet(0);
	private Set blacklists = new HashSet(0);
	private Set matchTimelyResultsForUseraId = new HashSet(0);
	private Set chatuserlists = new HashSet(0);
	private Set imageVisiables = new HashSet(0);

	// Constructors

	/** default constructor */
	public UserMetadata() {
	}

	/** minimal constructor */
	public UserMetadata(Integer userid, Integer university, String username, String password,
			String phonenum, String sex, String grade, String edueducation, String major,
			Short verificationMode, Boolean varificationPass) {
		this.userid = userid;
		this.university = university;
		this.username = username;
		this.password = password;
		this.phonenum = phonenum;
		this.sex = sex;
		this.grade = grade;
		this.edueducation = edueducation;
		this.major = major;
		this.verificationMode = verificationMode;
		this.varificationPass = varificationPass;
	}

	/** full constructor */
	public UserMetadata(Integer userid, Integer hometownCity, Integer currentCity,
			Integer university, String username, String nickname, String password, String phonenum,
			String registermail, String email, String sex, Date birthday, Short height,
			Short weight, String eduBackground, String grade, String edueducation, String major,
			String interests, String selfValue, String signature, String friendStandards,
			Short verificationMode, String constellation, Integer age, String headPortrait,
			String photoAlbum, String qqNumber, Boolean varificationPass,
			Set registerFriendsesForFriend1Id, Set matchTimelyResultsForUserbId,
			Set registerFriendsesForFriend2Id, Set registerScreenshots,
			Set registerFriendsesForUserid, Set recommendedTimelyResults, Set registerEmails,
			Set blacklists, Set matchTimelyResultsForUseraId, Set chatuserlists, Set imageVisiables) {
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
		this.varificationPass = varificationPass;
		this.registerFriendsesForFriend1Id = registerFriendsesForFriend1Id;
		this.matchTimelyResultsForUserbId = matchTimelyResultsForUserbId;
		this.registerFriendsesForFriend2Id = registerFriendsesForFriend2Id;
		this.registerScreenshots = registerScreenshots;
		this.registerFriendsesForUserid = registerFriendsesForUserid;
		this.recommendedTimelyResults = recommendedTimelyResults;
		this.registerEmails = registerEmails;
		this.blacklists = blacklists;
		this.matchTimelyResultsForUseraId = matchTimelyResultsForUseraId;
		this.chatuserlists = chatuserlists;
		this.imageVisiables = imageVisiables;
	}

	// Property accessors

	public Integer getUserid() {
		return this.userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public Integer gethometownCity() {
		return this.hometownCity;
	}

	public void sethometownCity(Integer hometownCity) {
		this.hometownCity = hometownCity;
	}

	public Integer getcurrentCity() {
		return this.currentCity;
	}

	public void setcurrentCity(Integer currentCity) {
		this.currentCity = currentCity;
	}

	public Integer getuniversity() {
		return this.university;
	}

	public void setuniversity(Integer university) {
		this.university = university;
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

	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Date getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Short getHeight() {
		return this.height;
	}

	public void setHeight(Short height) {
		this.height = height;
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

	public String getGrade() {
		return this.grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getEdueducation() {
		return this.edueducation;
	}

	public void setEdueducation(String edueducation) {
		this.edueducation = edueducation;
	}

	public String getMajor() {
		return this.major;
	}

	public void setMajor(String major) {
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

	public String getConstellation() {
		return this.constellation;
	}

	public void setConstellation(String constellation) {
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

	public Set getRegisterFriendsesForFriend1Id() {
		return this.registerFriendsesForFriend1Id;
	}

	public void setRegisterFriendsesForFriend1Id(Set registerFriendsesForFriend1Id) {
		this.registerFriendsesForFriend1Id = registerFriendsesForFriend1Id;
	}

	public Set getMatchTimelyResultsForUserbId() {
		return this.matchTimelyResultsForUserbId;
	}

	public void setMatchTimelyResultsForUserbId(Set matchTimelyResultsForUserbId) {
		this.matchTimelyResultsForUserbId = matchTimelyResultsForUserbId;
	}

	public Set getRegisterFriendsesForFriend2Id() {
		return this.registerFriendsesForFriend2Id;
	}

	public void setRegisterFriendsesForFriend2Id(Set registerFriendsesForFriend2Id) {
		this.registerFriendsesForFriend2Id = registerFriendsesForFriend2Id;
	}

	public Set getRegisterScreenshots() {
		return this.registerScreenshots;
	}

	public void setRegisterScreenshots(Set registerScreenshots) {
		this.registerScreenshots = registerScreenshots;
	}

	public Set getRegisterFriendsesForUserid() {
		return this.registerFriendsesForUserid;
	}

	public void setRegisterFriendsesForUserid(Set registerFriendsesForUserid) {
		this.registerFriendsesForUserid = registerFriendsesForUserid;
	}

	public Set getRecommendedTimelyResults() {
		return this.recommendedTimelyResults;
	}

	public void setRecommendedTimelyResults(Set recommendedTimelyResults) {
		this.recommendedTimelyResults = recommendedTimelyResults;
	}

	public Set getRegisterEmails() {
		return this.registerEmails;
	}

	public void setRegisterEmails(Set registerEmails) {
		this.registerEmails = registerEmails;
	}

	public Set getBlacklists() {
		return this.blacklists;
	}

	public void setBlacklists(Set blacklists) {
		this.blacklists = blacklists;
	}

	public Set getMatchTimelyResultsForUseraId() {
		return this.matchTimelyResultsForUseraId;
	}

	public void setMatchTimelyResultsForUseraId(Set matchTimelyResultsForUseraId) {
		this.matchTimelyResultsForUseraId = matchTimelyResultsForUseraId;
	}

	public Set getChatuserlists() {
		return this.chatuserlists;
	}

	public void setChatuserlists(Set chatuserlists) {
		this.chatuserlists = chatuserlists;
	}

	public Set getImageVisiables() {
		return this.imageVisiables;
	}

	public void setImageVisiables(Set imageVisiables) {
		this.imageVisiables = imageVisiables;
	}

}