package acs.elementBoundaryPackage;

public class Location {
	private double  lat;
	private double lng;
	
	public Location(double lat,double lng) {
		this.lat=lat;
		this.lng=lng;
	}
	public Location() {}
	
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double ing) {
		this.lng = ing;
	}
}