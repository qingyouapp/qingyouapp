package com.xwq.qingyouapp.bean;

public class RegisterFriends implements java.io.Serializable {

	// Fields

	private Integer userid;
	private UserMetadata userMetadataByFriend2Id;
	private UserMetadata userMetadataByUserid;
	private UserMetadata userMetadataByFriend1Id;

	// Constructors

	/** default constructor */
	public RegisterFriends() {
	}

	/** minimal constructor */
	public RegisterFriends(Integer userid, UserMetadata userMetadataByUserid) {
		this.userid = userid;
		this.userMetadataByUserid = userMetadataByUserid;
	}

	/** full constructor */
	public RegisterFriends(Integer userid,
			UserMetadata userMetadataByFriend2Id,
			UserMetadata userMetadataByUserid,
			UserMetadata userMetadataByFriend1Id) {
		this.userid = userid;
		this.userMetadataByFriend2Id = userMetadataByFriend2Id;
		this.userMetadataByUserid = userMetadataByUserid;
		this.userMetadataByFriend1Id = userMetadataByFriend1Id;
	}

	// Property accessors

	public Integer getUserid() {
		return this.userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public UserMetadata getUserMetadataByFriend2Id() {
		return this.userMetadataByFriend2Id;
	}

	public void setUserMetadataByFriend2Id(UserMetadata userMetadataByFriend2Id) {
		this.userMetadataByFriend2Id = userMetadataByFriend2Id;
	}

	public UserMetadata getUserMetadataByUserid() {
		return this.userMetadataByUserid;
	}

	public void setUserMetadataByUserid(UserMetadata userMetadataByUserid) {
		this.userMetadataByUserid = userMetadataByUserid;
	}

	public UserMetadata getUserMetadataByFriend1Id() {
		return this.userMetadataByFriend1Id;
	}

	public void setUserMetadataByFriend1Id(UserMetadata userMetadataByFriend1Id) {
		this.userMetadataByFriend1Id = userMetadataByFriend1Id;
	}

}