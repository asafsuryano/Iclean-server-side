package acs.newUserDetailsBoundaryPackage;


public class NewUserDetails {
	private String email;
	private String  role;
	private String username;
	private String avatar;
	
	public NewUserDetails() {
		
	}
	
	public NewUserDetails(String email, String username, String avatar, String role) {
		super();
		this.role = role;
		this.email = email;
		this.username = username;
		this.avatar = avatar;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	
	
}
