package acs.data.actionEntityProperties;

import javax.persistence.Embeddable;

import javax.persistence.Embedded;
@Embeddable
public class InvokedBy {
	private UserId userId;

	public InvokedBy() {}
	public InvokedBy(UserId userId) {
		super();
		this.userId = userId;
	}
	@Embedded
	public UserId getUserId() {
		return userId;
	}
	public void setUserId(UserId userId) {
		this.userId = userId;
	}
	
	
	
	@Override
	public String toString() {
		return userId.getDomain()+"#"+userId.getEmail();
	}
}
