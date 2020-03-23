package users;


public class NewUserDetails {
	private String email;
	private Roles role;
	private String username;
	private String avatar;
	
	public NewUserDetails() {
		
	}
	
	public NewUserDetails(String email, String username, String avatar, Roles role) {
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
	
	public Roles getRole() {
		return role;
	}

	public void setRole(Roles role) {
		this.role = role;
	}

	
	
}
