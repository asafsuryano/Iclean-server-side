package acs.data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import acs.data.elementEntityProperties.ElementId;



@Entity
public class ParenToChildEntity {
	public ParenToChildEntity() {}
	
	
@EmbeddedId
@ManyToOne
@JoinColumn(name="id")
private ElementId ParentId;

@EmbeddedId
@ManyToOne
@JoinColumn(name = "id")
private  ElementId childId;

public ElementId getParentId() {
	return ParentId;
}

public void setParentId(ElementId parentId) {
	ParentId = parentId;
}

public ElementId getChildId() {
	return childId;
}

public void setChildId(ElementId childId) {
	this.childId = childId;
}
}
