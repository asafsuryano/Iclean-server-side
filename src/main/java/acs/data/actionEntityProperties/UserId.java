package acs.data.actionEntityProperties;

public class UserId {
	private String domain;
	private String email;
	public UserId(String domain, String email) {
		super();
		this.domain = domain;
		this.email = email;
	}
	public UserId()
	{
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
