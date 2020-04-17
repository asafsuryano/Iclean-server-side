package acs.data;

import java.util.Date;

import acs.data.elementEntityProperties.*;
import acs.elementBoundaryPackage.Type;

public class ElementEntity {
	private ElementId elementId;
	private Type type;
	private String name;
	private Date createdTimeStamp;
	private Location location;
	private UserId createdBy;// ask about if createdBy is a class of its own
	private int levelOfDirt;
	private boolean isActive;
	public ElementEntity() {}
	public ElementId getElementId() {
		return elementId;
	}
	public void setElementId(ElementId elementId) {
		this.elementId = elementId;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
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
	public int getLevelOfDirt() {
		return levelOfDirt;
	}
	public void setLevelOfDirt(int levelOfDirt) {
		this.levelOfDirt = levelOfDirt;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}
