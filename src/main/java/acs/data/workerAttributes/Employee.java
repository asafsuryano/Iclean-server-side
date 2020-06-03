package acs.data.workerAttributes;

import java.util.Map;

public class Employee {
	private String name;
	private String phoneNumber;
	public Employee(String name, String phoneNumber) {
		super();
		this.name = name;
		this.phoneNumber = phoneNumber;
	}
	public Employee(Map<String, Object> map) {
		this.name=(String)map.get("name");
		this.phoneNumber=(String)map.get("phoneNumber");
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	
}
