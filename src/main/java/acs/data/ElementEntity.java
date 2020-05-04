package acs.data;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
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
	private UserId createdBy;
	private boolean isActive;
	private Map<String,Object> elementAttributes;
	private Set<ElementEntity> children;
//	@ManyToMany
//	@JoinTable(name="id",
//    joinColumns=@JoinColumn(name="parent"),
//    inverseJoinColumns=@JoinColumn(name="children2")
//    )
//	private Collection<ElementEntity> parents ; 
//	
//	@ManyToMany(mappedBy = "Parents",cascade = CascadeType.PERSIST)
//	private Collection<ElementEntity> children2;



	
	
	
	
	private ElementEntity parent;
	
	public ElementEntity() {
		//ben test12
	}
	
	@Id
	@Embedded
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
	
	
	@ManyToOne
	public ElementEntity getParent() {
		return parent;
	}

	public void setParent(ElementEntity parent) {
		if(parent != null) {
			this.parent = parent;
			parent.getChildren().add(this);
		}
	}
	
	
	@OneToMany(mappedBy = "parent")
	public Set<ElementEntity> getChildren() {
		return children;
	}

	public void setChildren(Set<ElementEntity> children) {
		this.children = children;
	}
	
}