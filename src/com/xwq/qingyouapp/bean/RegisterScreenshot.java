package com.xwq.qingyouapp.bean;



/**
 * RegisterScreenshot entity. @author MyEclipse Persistence Tools
 */
public class RegisterScreenshot implements java.io.Serializable {

	// Fields

	private Integer userid;
	private UserMetadata userMetadata;
	private String registerScreenshot;

	// Constructors

	/** default constructor */
	public RegisterScreenshot() {
	}

	/** minimal constructor */
	public RegisterScreenshot(Integer userid, UserMetadata userMetadata) {
		this.userid = userid;
		this.userMetadata = userMetadata;
	}

	/** full constructor */
	public RegisterScreenshot(Integer userid, UserMetadata userMetadata,
			String registerScreenshot) {
		this.userid = userid;
		this.userMetadata = userMetadata;
		this.registerScreenshot = registerScreenshot;
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

	public String getRegisterScreenshot() {
		return this.registerScreenshot;
	}

	public void setRegisterScreenshot(String registerScreenshot) {
		this.registerScreenshot = registerScreenshot;
	}

}