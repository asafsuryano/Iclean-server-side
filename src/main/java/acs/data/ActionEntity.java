package acs.data;

import java.util.Date;
import java.util.HashMap;

import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acs.dal.MapToJsonConverter;
import acs.data.actionEntityProperties.ActionId;
import acs.data.actionEntityProperties.Element;
import acs.data.actionEntityProperties.InvokedBy;

@Entity
@Table(name="Actions")
public class ActionEntity {
	private String type;
	private Date createdTimestamp;
	private ActionId actionId;
	private String element;
	private String invokedBy;
	private HashMap<String, Object> actionAttributes;
	
	
	public ActionEntity() {}
	public ActionEntity(String type, Date createdTimestamp, ActionId actionId, Element element, InvokedBy invokedBy,
			HashMap<String, Object> actionAttributes) {
		super();
		this.type = type;
		this.createdTimestamp = createdTimestamp;
		this.actionId = actionId;
		this.element = element.toString();
		this.invokedBy = invokedBy.toString();
		this.actionAttributes = actionAttributes;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}
	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}
	
	@EmbeddedId
	public ActionId getActionId() {
		return actionId;
	}
	public void setActionId(ActionId actionId) {
		this.actionId = actionId;
	}
	
	
	
	public String getElement() {
		return element;
	}
	public void setElement(Element element) {
		this.element = element.toString();
	}
	
	
	
	public String getInvokedBy() {
		return invokedBy;
	}
	public void setInvokedBy(InvokedBy invokedBy) {
		this.invokedBy = invokedBy.toString();
	}
	
	
	@Convert(converter=MapToJsonConverter.class)
	@Lob
	public HashMap<String, Object> getActionAttributes() {
		return actionAttributes;
	}
	public void setActionAttributes(HashMap<String, Object> actionAttributes) {
		this.actionAttributes = actionAttributes;
	}
	
	
}
