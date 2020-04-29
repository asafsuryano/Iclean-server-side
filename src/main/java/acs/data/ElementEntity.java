package acs.data;

import java.util.Date;
import java.util.LinkedHashMap;

import acs.data.elementEntityProperties.*;
import acs.data.elementEntityProperties.ElementId;
import acs.data.elementEntityProperties.Location;

public class ElementEntity {
	private ElementId elementId;
	private String type;
	private String name;
	private Date createdTimeStamp;
	private Location location;
	private UserId createdBy;
	private boolean isActive;
	private LinkedHashMap<String,Object> elementAttributes;
	
	public ElementEntity() {
		//ben test
	}
	
	
	public ElementId getElementId() {
		return elementId;
	}
	public void setElementId(ElementId elementId) {
		this.elementId = elementId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreatedTimeStamp() {
		return createdTimeStamp;
	}
	public void setCreatedTimeStamp(Date createdTimeStamp) {
		this.createdTimeStamp = createdTimeStamp;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public UserId getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(UserId createdBy) {
		this.createdBy = createdBy;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public LinkedHashMap<String, Object> getElementAttributes() {
		return elementAttributes;
	}
	public void setElementAttributes(LinkedHashMap<String, Object> elementAttributes) {
		this.elementAttributes = elementAttributes;
	}
	
}
