package ActionBoundaryClasses;

public class InvokedBy {
	private UserId userId;
	public InvokedBy() {}
	public InvokedBy(UserId userId) {
		super();
		this.userId = userId;
	}
	public UserId getUserId() {
		return userId;
	}
	public void setUserId(UserId userId) {
		this.userId = userId;
	}
	
}
