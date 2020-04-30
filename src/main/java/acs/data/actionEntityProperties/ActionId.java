package acs.data.actionEntityProperties;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class ActionId implements Serializable  {
	private static final long serialVersionUID = 162648946937469637L;
	private String domain;
	private String id;
	public ActionId() {
	}
	public ActionId(String domain, String id) {
		super();
		this.domain = domain;
		this.id = id;
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		ActionId other = (ActionId) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
