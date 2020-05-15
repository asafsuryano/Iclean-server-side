package acs.data.elementEntityProperties;

import javax.persistence.Embeddable;

@Embeddable
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
	public double getIng() {
		return lng;
	}
	public void setIng(double ing) {
		this.lng = ing;
	}
}
