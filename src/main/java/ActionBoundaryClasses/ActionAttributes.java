package ActionBoundaryClasses;

public class ActionAttributes {
	private String key1;
	private double key2;
	private boolean booleanValue;
	private String lastKey;
	public ActionAttributes(String key1, double key2, boolean booleanValue, String lastKey) {
		super();
		this.key1 = key1;
		this.key2 = key2;
		this.booleanValue = booleanValue;
		this.lastKey = lastKey;
	}
	public ActionAttributes() {
		super();
	}
	public String getKey1() {
		return key1;
	}
	public void setKey1(String key1) {
		this.key1 = key1;
	}
	public double getKey2() {
		return key2;
	}
	public void setKey2(double key2) {
		this.key2 = key2;
	}
	public boolean isBooleanValue() {
		return booleanValue;
	}
	public void setBooleanValue(boolean booleanValue) {
		this.booleanValue = booleanValue;
	}
	public String getLastKey() {
		return lastKey;
	}
	public void setLastKey(String lastKey) {
		this.lastKey = lastKey;
	}
	
	
}
