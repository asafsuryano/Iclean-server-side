	package acs.elementBoundaryPackage;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class ElementBoundary {
   private String name;
   private ElementId elementId; 
   private String type;
   private Boolean active;
   private Date date;
   private CreatedBy createdby;
   private Location location;
   private Map<String,Object> elementAttributes;
   
  
public ElementBoundary(String name, ElementId eID,String type,Boolean active,Date date,CreatedBy CBy,Location location, LinkedHashMap<String, Object> elementAttributes) {
	   this.elementId=eID;
	   this.active=active;
	   this.type=type;
	   this.date=date;
	   this.createdby=CBy;
	   this.location=location;
	   this.elementAttributes=elementAttributes;
	   this.name = name;
	   
   }
   public ElementBoundary() {
	   
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

	public Boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public CreatedBy getCreatedby() {
		return createdby;
	}
	public void setCreatedby(CreatedBy createdby) {
		this.createdby = createdby;
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
