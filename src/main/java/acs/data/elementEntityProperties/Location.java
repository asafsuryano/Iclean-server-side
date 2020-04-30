package acs.data.elementEntityProperties;

import javax.persistence.Embeddable;

@Embeddable
public class Location {
	private double  lat;
	private double ing;

	public Location(double lat,double ing) {
		this.lat=lat;
		this.ing=ing;
	}
	public Location() {}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getIng() {
		return ing;
	}
	public void setIng(double ing) {
		this.ing = ing;
	}
}
