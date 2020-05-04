package acs.data;

import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


import acs.data.userEntityProperties.User;


@Entity
public class UserEntity {
	private User userId;
	private UserRoles role;
	private String username;
	private String avatar;
	private boolean isDeleted;
	private Date timestemp;
	
	
	
	public UserEntity(User userId, UserRoles role, String username, String avatar) {
		this.userId = userId;
		//this.role = role;
		this.username = username;
		this.avatar = avatar;
		this.role=UserRoles.PLAYER;
	}
	public UserEntity() {};


	@EmbeddedId
	public User getUserId() {
		return userId;
	}

	public void setUserId(User user) {
		this.userId = user;
	}
    @Enumerated(EnumType.ORDINAL)
	public UserRoles getRole() {
		return role;
	}
	
	public void setRole(UserRoles role) {
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
	@Temporal(TemporalType.TIMESTAMP)
	public Date getTimestemp() {
		return timestemp;
	}

	public void setTimestemp(Date timestemp) {
		this.timestemp = timestemp;
	}



}
