package com.xwq.qingyouapp.bean;



/**
 * RecommendedTimelyResult entity. @author MyEclipse Persistence Tools
 */
public class RecommendedTimelyResult implements java.io.Serializable {

	// Fields

	private Integer userid;
	private UserMetadata userMetadata;
	private String recommendedFriends;

	// Constructors

	/** default constructor */
	public RecommendedTimelyResult() {
	}

	/** minimal constructor */
	public RecommendedTimelyResult(Integer userid, UserMetadata userMetadata) {
		this.userid = userid;
		this.userMetadata = userMetadata;
	}

	/** full constructor */
	public RecommendedTimelyResult(Integer userid, UserMetadata userMetadata,
			String recommendedFriends) {
		this.userid = userid;
		this.userMetadata = userMetadata;
		this.recommendedFriends = recommendedFriends;
	}

	// Property accessors

	public Integer getUserid() {
		return this.userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public UserMetadata getUserMetadata() {
		return this.userMetadata;
	}

	public void setUserMetadata(UserMetadata userMetadata) {
		this.userMetadata = userMetadata;
	}

	public String getRecommendedFriends() {
		return this.recommendedFriends;
	}

	public void setRecommendedFriends(String recommendedFriends) {
		this.recommendedFriends = recommendedFriends;
	}

}