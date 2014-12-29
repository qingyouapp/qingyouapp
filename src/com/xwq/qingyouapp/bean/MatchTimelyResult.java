package com.xwq.qingyouapp.bean;


public class MatchTimelyResult implements java.io.Serializable {

	// Fields

	private MatchTimelyResultId id;
	private Integer score;

	// Constructors

	/** default constructor */
	public MatchTimelyResult() {
	}

	/** minimal constructor */
	public MatchTimelyResult(MatchTimelyResultId id) {
		this.id = id;
	}

	/** full constructor */
	public MatchTimelyResult(MatchTimelyResultId id, Integer score) {
		this.id = id;
		this.score = score;
	}

	// Property accessors

	public MatchTimelyResultId getId() {
		return this.id;
	}

	public void setId(MatchTimelyResultId id) {
		this.id = id;
	}

	public Integer getScore() {
		return this.score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

}