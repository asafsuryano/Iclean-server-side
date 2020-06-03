package acs.data.reportsAttributes;

import java.util.Comparator;

public class ReportsComparator implements Comparator<Report> {

	@Override
	public int compare(Report o1, Report o2) {
		return (o1.getCreatedTimeStamp().compareTo(o2.getCreatedTimeStamp()));
	}

}
