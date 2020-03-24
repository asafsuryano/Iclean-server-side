package ElementPackage;

public class CreatedBy {
//need to add user id;\

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
