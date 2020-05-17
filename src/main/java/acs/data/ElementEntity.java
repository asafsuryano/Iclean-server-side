package acs.data;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acs.dal.MapToJsonConverter;
import acs.data.elementEntityProperties.*;
import acs.data.elementEntityProperties.ElementId;
import acs.data.elementEntityProperties.Location;

@Entity
@Table(name="Element")
public class ElementEntity {
	private ElementId elementId;
	private String type;
	private String name;
	private Date createdTimeStamp;
	private Location location;
	//private double lat;
	private UserId createdBy;
	private boolean isActive;
	private Map<String,Object> elementAttributes;
	private Set<ElementEntity> children;	
	private ElementEntity parent;
	
	public ElementEntity() {
		children = new HashSet<ElementEntity>();
		elementAttributes = new HashMap<String, Object>();
	}
	
	
	
	@EmbeddedId
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
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreatedTimeStamp() {
		return createdTimeStamp;
	}
	public void setCreatedTimeStamp(Date createdTimeStamp) {
		this.createdTimeStamp = createdTimeStamp;
	}
	
	
	@Embedded
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	
	
	@Embedded
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
	
	@Convert (converter = MapToJsonConverter.class)
	@Lob
	public Map<String, Object> getElementAttributes() {
		return elementAttributes;
	}
	public void setElementAttributes(Map<String, Object> elementAttributes) {
		this.elementAttributes = elementAttributes;
	}
	
	//public void addChildren(ElementEntity child) {
		//this.children.contains(z)
	//}
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	public ElementEntity getParent() {
		return parent;
	}

	public void setParent(ElementEntity parent) {
		
		if(parent != null) {
			if (this.parent!=null)
				if (this.parent.getChildren().contains(this))
					this.parent.getChildren().remove(this);
			parent.getChildren().add(this);
		}//else {
			//this.parent=null;
		//}
		this.parent = parent;
	}
	
	
	@OneToMany(mappedBy = "parent" ,fetch = FetchType.LAZY)
	public Set<ElementEntity> getChildren() {
		return children;
	}

	public void setChildren(Set<ElementEntity> children) {
		//for (ElementEntity elementEntity : this.children) {
			//elementEntity.setParent(null);
		//}
		this.children = children;
	}
	
}