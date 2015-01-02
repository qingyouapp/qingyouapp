package com.xwq.qingyouapp.bean;


/**
 * Chatuserlist entity. @author MyEclipse Persistence Tools
 */
public class Chatuserlist implements java.io.Serializable {

	// Fields

	private Integer userId;
	private UserMetadata userMetadata;
	private String chatFriends;

	// Constructors

	/** default constructor */
	public Chatuserlist() {
	}

	/** minimal constructor */
	public Chatuserlist(Integer userId, UserMetadata userMetadata) {
		this.userId = userId;
		this.userMetadata = userMetadata;
	}

	/** full constructor */
	public Chatuserlist(Integer userId, UserMetadata userMetadata,
			String chatFriends) {
		this.userId = userId;
		this.userMetadata = userMetadata;
		this.chatFriends = chatFriends;
	}

	// Property accessors

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public UserMetadata getUserMetadata() {
		return this.userMetadata;
	}

	public void setUserMetadata(UserMetadata userMetadata) {
		this.userMetadata = userMetadata;
	}

	public String getChatFriends() {
		return this.chatFriends;
	}

	public void setChatFriends(String chatFriends) {
		this.chatFriends = chatFriends;
	}

}