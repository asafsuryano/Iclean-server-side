package ElementPackage;

public class UserId {
	private String Userdomain;
	private String UserEmail;

	public UserId(String domain, String email) {
		this.Userdomain = domain;
		this.UserEmail = email;
	}
	public String getUserdomain() {
		return Userdomain;
	}
	public void setUserdomain(String userdomain) {
		Userdomain = userdomain;
	}
	public String getUserEmail() {
		return UserEmail;
	}
	public void setUserEmail(String userEmail) {
		UserEmail = userEmail;
	}
	public UserId() {}
}
