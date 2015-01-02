package com.xwq.qingyouapp.bean;



/**
 * RegisterEmail entity. @author MyEclipse Persistence Tools
 */
public class RegisterEmail implements java.io.Serializable {

	// Fields

	private Integer userid;
	private UserMetadata userMetadata;
	private String eduEmail;

	// Constructors

	/** default constructor */
	public RegisterEmail() {
	}

	/** full constructor */
	public RegisterEmail(Integer userid, UserMetadata userMetadata,
			String eduEmail) {
		this.userid = userid;
		this.userMetadata = userMetadata;
		this.eduEmail = eduEmail;
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

	public String getEduEmail() {
		return this.eduEmail;
	}

	public void setEduEmail(String eduEmail) {
		this.eduEmail = eduEmail;
	}

}