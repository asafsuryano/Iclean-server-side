package acs.data;

import java.util.Date;
import java.util.Map;

import acs.data.userEntityProperties.User;



public class UserEntity {
	private User userId;
	private String role;
	private String username;
	private String avatar;
	private boolean isDeleted;
	private Date timestemp;
	private Map<String, Object> details;
	
	
	
	public UserEntity(User userId, String role, String username, String avatar) {
		this.userId = userId;
		this.role = role;
		this.username = username;
		this.avatar = avatar;
	}
	public UserEntity() {};


	public User getUserId() {
		return userId;
	}

	public void setUserId(User user) {
		this.userId = user;
	}

	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
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

	public boolean getDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Date getTimestemp() {
		return timestemp;
	}

	public void setTimestemp(Date timestemp) {
		this.timestemp = timestemp;
	}

	public Map<String, Object> getDetails() {
		return details;
	}

	public void setDetails(Map<String, Object> details) {
		this.details = details;
	}


}
