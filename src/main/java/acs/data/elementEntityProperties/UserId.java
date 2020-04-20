package acs.data.elementEntityProperties;


public class UserId {
	private String domain;
	private String email;
	public UserId(String domain,String email) {
		setDomain(domain);
		setEmail(email);
	}
	public UserId() {
		
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}
