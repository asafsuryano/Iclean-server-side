package acs.elementBoundaryPackage;

public class CreatedBy {

	private UserId userId;

	public UserId getUserId() {
		return userId;
	}

	public void setUserId(UserId userId) {
		this.userId = userId;
	}

	public CreatedBy(UserId userId) {
		this.userId = userId;

	}

	public CreatedBy() {
	}

}
