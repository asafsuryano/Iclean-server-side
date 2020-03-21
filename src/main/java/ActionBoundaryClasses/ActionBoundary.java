package ActionBoundaryClasses;

public class ActionBoundary {
	private String type;
	private String createdTimestamp;
	private ActionId actionId;
	private Element element;
	private InvokedBy invokedBy;
	private ActionAttributes actionAttributes;
	public ActionBoundary(String type, String createdTimestamp, ActionId actionId, Element element, InvokedBy invokedBy,
			ActionAttributes actionAttributes) {
		super();
		this.type = type;
		this.createdTimestamp = createdTimestamp;
		this.actionId = actionId;
		this.element = element;
		this.invokedBy = invokedBy;
		this.actionAttributes = actionAttributes;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCreatedTimestamp() {
		return createdTimestamp;
	}
	public void setCreatedTimestamp(String createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}
	public ActionId getActionId() {
		return actionId;
	}
	public void setActionId(ActionId actionId) {
		this.actionId = actionId;
	}
	public Element getElement() {
		return element;
	}
	public void setElement(Element element) {
		this.element = element;
	}
	public InvokedBy getInvokedBy() {
		return invokedBy;
	}
	public void setInvokedBy(InvokedBy invokedBy) {
		this.invokedBy = invokedBy;
	}
	public ActionAttributes getActionAttributes() {
		return actionAttributes;
	}
	public void setActionAttributes(ActionAttributes actionAttributes) {
		this.actionAttributes = actionAttributes;
	}
	
	
}
