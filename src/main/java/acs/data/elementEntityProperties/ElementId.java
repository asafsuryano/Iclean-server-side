package acs.data.elementEntityProperties;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class ElementId implements Comparable<ElementId>,Serializable {
	private static final long serialVersionUID = -1916704765505760714L;
	private String domain;
	private String id;

	public ElementId(String domain, String id) {
		this.domain = domain;
		this.id = id;
	}

	public ElementId() {}

	public String getElementDomain() {
		return domain;
	}

	public void setElementDomain(String elementDomain) {
		this.domain = elementDomain;
	}

	public String getElementId() {
		return id;
	}

	public void setElementId(String elementId) {
		id = elementId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((domain == null) ? 0 : domain.hashCode());
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (domain == null) {
			if (other.domain != null)
				return false;
		} else if (!domain.equals(other.domain))
			return false;
		return true;
	}

	@Override
	public int compareTo(ElementId o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
