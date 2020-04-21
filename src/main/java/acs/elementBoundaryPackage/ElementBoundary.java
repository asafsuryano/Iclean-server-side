	package acs.elementBoundaryPackage;

import java.util.Date;
import java.util.HashMap;
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
   
   
   public ElementBoundary(ElementId eID,String type,Boolean active,Date date,CreatedBy CBy,Location location) {
	   this.elementId=eID;
	   this.active=active;
	   this.type=type;
	   this.date=date;
	   this.createdby=CBy;
	   this.location=location;
	   elementAttributes=new HashMap<String,Object>();
	   
   }
   public ElementBoundary() {
	   this.elementAttributes = new HashMap<>();
   }
   
   public ElementBoundary(String userDomain,String userEmail,String elemnetDomain,String elementID) {	   
	  this.createdby=new CreatedBy(new UserId(userDomain, userEmail));
	  this.elementId=new ElementId(elemnetDomain,elementID);
	   this.elementAttributes=new HashMap<String,Object>();	  
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
public Map<String, Object> getElementAttribute() {
	return elementAttributes;
}
public void setElementAttrbiutes(Map<String, Object> elemetnAttrbiutes) {
	this.elementAttributes = elemetnAttrbiutes;
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
   
}
