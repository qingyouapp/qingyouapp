package com.xwq.qingyouapp.bean;


public class MatchTimelyResultId implements java.io.Serializable {

	// Fields

	private UserMetadata userMetadata;
	private UserMetadata userMetadata_1;

	// Constructors

	/** default constructor */
	public MatchTimelyResultId() {
	}

	/** full constructor */
	public MatchTimelyResultId(UserMetadata userMetadata,
			UserMetadata userMetadata_1) {
		this.userMetadata = userMetadata;
		this.userMetadata_1 = userMetadata_1;
	}

	// Property accessors

	public UserMetadata getUserMetadata() {
		return this.userMetadata;
	}

	public void setUserMetadata(UserMetadata userMetadata) {
		this.userMetadata = userMetadata;
	}

	public UserMetadata getUserMetadata_1() {
		return this.userMetadata_1;
	}

	public void setUserMetadata_1(UserMetadata userMetadata_1) {
		this.userMetadata_1 = userMetadata_1;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof MatchTimelyResultId))
			return false;
		MatchTimelyResultId castOther = (MatchTimelyResultId) other;

		return ((this.getUserMetadata() == castOther.getUserMetadata()) || (this
				.getUserMetadata() != null
				&& castOther.getUserMetadata() != null && this
				.getUserMetadata().equals(castOther.getUserMetadata())))
				&& ((this.getUserMetadata_1() == castOther.getUserMetadata_1()) || (this
						.getUserMetadata_1() != null
						&& castOther.getUserMetadata_1() != null && this
						.getUserMetadata_1().equals(
								castOther.getUserMetadata_1())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getUserMetadata() == null ? 0 : this.getUserMetadata()
						.hashCode());
		result = 37
				* result
				+ (getUserMetadata_1() == null ? 0 : this.getUserMetadata_1()
						.hashCode());
		return result;
	}

}