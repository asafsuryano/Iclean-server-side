package acs.usersBoundaryPackage;

public class UserBoundary {
	private User userId;
	private String role;
	private String username;
	private String avatar;
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


}