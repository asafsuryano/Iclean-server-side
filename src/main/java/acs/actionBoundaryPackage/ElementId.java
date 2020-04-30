package acs.actionBoundaryPackage;

public class ElementId {
	private String domain;
	private String id;
	public ElementId() {
	}
	public ElementId(String domain, String id) {
		super();
		this.domain = domain;
		this.id = id;
	}
	public ElementId(String allInfo) {
		String [] array=allInfo.split("#");
		this.domain=array[0];
		this.id=array[1];
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
