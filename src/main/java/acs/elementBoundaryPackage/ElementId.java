package acs.elementBoundaryPackage;

public class ElementId {
	private String elementDomain;
	private String ElementId;

	public ElementId(String domain, String id) {
		this.elementDomain = domain;
		this.ElementId = id;
	}

	public ElementId() {}

	public String getElementDomain() {
		return elementDomain;
	}

	public void setElementDomain(String elementDomain) {
		this.elementDomain = elementDomain;
	}

	public String getElementId() {
		return ElementId;
	}

	public void setElementId(String elementId) {
		ElementId = elementId;
	}
}
