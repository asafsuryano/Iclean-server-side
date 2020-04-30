package acs.elementBoundaryPackage;

public class UserId {
	private String domain;
	private String email;

	public UserId(String domain, String email) {
		this.domain = domain;
		this.email = email;
	}
	public String getUserdomain() {
		return domain;
	}
	public void setUserdomain(String userdomain) {
		domain = userdomain;
	}
	public String getUserEmail() {
		return email;
	}
	public void setUserEmail(String userEmail) {
		email = userEmail;
	}
	public UserId() {}
}
