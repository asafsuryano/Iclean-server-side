package acs.data.reportsAttributes;

import java.util.Date;

public class Report {
	private int trashLevel;
	private String comment;
	private Date createdTimeStamp;
	private String userId;
	
	public Report() {}

	public Report(int trashLevel, String comment, Date createdTimeStamp, String userId) {
		super();
		this.trashLevel = trashLevel;
		this.comment = comment;
		this.createdTimeStamp = createdTimeStamp;
		this.userId = userId;
	}
	
	//public Report(Map<String,>)

	public int getTrashLevel() {
		return trashLevel;
	}

	public void setTrashLevel(int trashLevel) {
		this.trashLevel = trashLevel;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getCreatedTimeStamp() {
		return createdTimeStamp;
	}

	public void setCreatedTimeStamp(Date createdTimeStamp) {
		this.createdTimeStamp = createdTimeStamp;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
