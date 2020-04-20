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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ElementId == null) ? 0 : ElementId.hashCode());
		result = prime * result + ((elementDomain == null) ? 0 : elementDomain.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ElementId other = (ElementId) obj;
		if (ElementId == null) {
			if (other.ElementId != null)
				return false;
		} else if (!ElementId.equals(other.ElementId))
			return false;
		if (elementDomain == null) {
			if (other.elementDomain != null)
				return false;
		} else if (!elementDomain.equals(other.elementDomain))
			return false;
		return true;
	}
	
	
}
