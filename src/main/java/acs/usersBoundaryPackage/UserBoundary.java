package acs.usersBoundaryPackage;

import java.util.Date;
import java.util.Map;

public class UserBoundary {
	private User userId;
	private String role;
	private String username;
	private Boolean isDeleted;
	private String avatar;
	private Map<String, Object> details;
	private Date timestamp;
	
	

	public UserBoundary(User userId, String roles, String username, String avatar) {
		super();
		this.userId = userId;
		this.role = roles;
		this.username = username;
		this.avatar = avatar;
	}
	public UserBoundary()
	{
		
	}


	public Map<String, Object> getDetails() {
		return details;
	}

	public void setDetails(Map<String, Object> details) {
		this.details = details;
	}

	
	public User getUserId() {
		return userId;
	}

	public void setUserId(User userId) {
		this.userId = userId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String  role) {
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public Boolean getDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
}