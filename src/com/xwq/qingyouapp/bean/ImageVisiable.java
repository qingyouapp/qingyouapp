package com.xwq.qingyouapp.bean;


public class ImageVisiable implements java.io.Serializable {

	// Fields

	private Integer userId;
	private UserMetadata userMetadata;
	private String imageVisiableFriends;

	// Constructors

	/** default constructor */
	public ImageVisiable() {
	}

	/** minimal constructor */
	public ImageVisiable(Integer userId, UserMetadata userMetadata) {
		this.userId = userId;
		this.userMetadata = userMetadata;
	}

	/** full constructor */
	public ImageVisiable(Integer userId, UserMetadata userMetadata,
			String imageVisiableFriends) {
		this.userId = userId;
		this.userMetadata = userMetadata;
		this.imageVisiableFriends = imageVisiableFriends;
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

	public String getImageVisiableFriends() {
		return this.imageVisiableFriends;
	}

	public void setImageVisiableFriends(String imageVisiableFriends) {
		this.imageVisiableFriends = imageVisiableFriends;
	}

}