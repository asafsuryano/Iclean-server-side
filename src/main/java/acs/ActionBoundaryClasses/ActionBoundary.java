package acs.ActionBoundaryClasses;


import java.util.Date;
import java.util.HashMap;


public class ActionBoundary {
	private String type;
	private Date createdTimestamp;
	private ActionId actionId;
	private Element element;
	private InvokedBy invokedBy;
	private HashMap<String, Object> actionAttributes;
	
	public ActionBoundary() {}
	public ActionBoundary(String type, Date createdTimestamp, ActionId actionId, Element element, InvokedBy invokedBy,
			HashMap<String, Object> actionAttributes) {
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
	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}
	public void setCreatedTimestamp(Date createdTimestamp) {
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
	public HashMap<String, Object> getActionAttributes() {
		return actionAttributes;
	}
	public void setActionAttributes(HashMap<String, Object> actionAttributes) {
		this.actionAttributes = actionAttributes;
	}
	
	
}
