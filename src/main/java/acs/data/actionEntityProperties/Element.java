package acs.data.actionEntityProperties;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
@Embeddable
public class Element {
	
	
	private ElementId elementId;

	public Element() {}
	
	public Element( ElementId elementId) {
		super();
         this.elementId=elementId;
		
	}
	
    @Embedded
	public ElementId getElementId() {
		return elementId;
	}

	public void setElementId(ElementId elementId) {
		this.elementId = elementId;
	}

	@Override
	public String toString() {
		return elementId.getDomain()+"#"+elementId.getId();
	}
	

}
