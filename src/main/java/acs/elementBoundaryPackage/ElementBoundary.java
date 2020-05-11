package acs.elementBoundaryPackage;

import java.util.Date;
import java.util.Map;

public class ElementBoundary {
   private String name;
   private ElementIdBoundary elementId; 
   private String type;
   private Boolean active;
   private Date createdTimestamp;
   private CreatedBy createdBy;
   private Location location;
   private Map<String,Object> elementAttributes;
   
  
public ElementBoundary(String name, ElementIdBoundary eID,String type,Boolean active,Date date,CreatedBy CBy,Location location, Map<String, Object> elementAttributes) {
	   this.elementId=eID;
	   this.active=active;
	   this.type=type;
	   this.createdTimestamp=date;
	   this.createdBy=CBy;
	   this.location=location;
	   this.elementAttributes=elementAttributes;
	   this.name = name;
	   
   }
   public ElementBoundary() {
	   
   }

	public ElementIdBoundary getElementId() {
		return elementId;
	}
	public void setElementId(ElementIdBoundary elementId) {
		this.elementId = elementId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public Boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public Date getDate() {
		return createdTimestamp;
	}
	public void setDate(Date createdTimeStamp) {
		this.createdTimestamp = createdTimeStamp;
	}
	public CreatedBy getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(CreatedBy createdby) {
		this.createdBy = createdby;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	 
   public Map<String,Object> getElementAttributes() {
	   	return elementAttributes;
	}
	public void setElementAttributes(Map<String,Object> elementAttributes) {
		this.elementAttributes = elementAttributes;
	}
	   
}
