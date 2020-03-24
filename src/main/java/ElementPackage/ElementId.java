package ElementPackage;

public class ElementId {
	private String elementDomain;
	private int ElementId;

	public ElementId(String domain, int id) {
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

	public int getElementId() {
		return ElementId;
	}

	public void setElementId(int elementId) {
		ElementId = elementId;
	}
}
