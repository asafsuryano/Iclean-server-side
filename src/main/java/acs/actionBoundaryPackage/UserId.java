package acs.actionBoundaryPackage;

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
	public UserId(String allInfo) {
		String [] array=allInfo.split("#");
		this.domain=array[0];
		this.email=array[1];
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
	@Override
	public String toString() {
		return "[domain=" + domain + ", email=" + email + "]";
	}
	
	
	
	
	
}
