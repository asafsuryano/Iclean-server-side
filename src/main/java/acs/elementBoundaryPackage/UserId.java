package acs.elementBoundaryPackage;

public class UserId {
	private String domain;
	private String email;

	public UserId(String domain, String email) {
		this.domain = domain;
		this.email = email;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String userdomain) {
		domain = userdomain;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String userEmail) {
		email = userEmail;
	}
	public UserId() {}
}
