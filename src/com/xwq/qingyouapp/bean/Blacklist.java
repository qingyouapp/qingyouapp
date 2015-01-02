package com.xwq.qingyouapp.bean;



/**
 * Blacklist entity. @author MyEclipse Persistence Tools
 */
public class Blacklist implements java.io.Serializable {

	// Fields

	private Integer userid;
	private UserMetadata userMetadata;
	private String blacklist;

	// Constructors

	/** default constructor */
	public Blacklist() {
	}

	/** minimal constructor */
	public Blacklist(Integer userid, UserMetadata userMetadata) {
		this.userid = userid;
		this.userMetadata = userMetadata;
	}

	/** full constructor */
	public Blacklist(Integer userid, UserMetadata userMetadata, String blacklist) {
		this.userid = userid;
		this.userMetadata = userMetadata;
		this.blacklist = blacklist;
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

	public String getBlacklist() {
		return this.blacklist;
	}

	public void setBlacklist(String blacklist) {
		this.blacklist = blacklist;
	}

}