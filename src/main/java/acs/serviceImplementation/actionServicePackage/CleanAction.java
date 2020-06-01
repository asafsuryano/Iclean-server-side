package acs.serviceImplementation.actionServicePackage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import acs.actionBoundaryPackage.ActionBoundary;
import acs.data.reportsAttributes.Report;
import acs.logic.UserService;
import acs.logic.ExtraElementsService;

public class CleanAction extends Action {

	public CleanAction(UserService userService, ExtraElementsService elementService, ActionBoundary action) {
		super(userService, elementService, action);
		// TODO Auto-generated constructor stub
	}

	@Override
	void invoke() {
		try {
		super.init();
		ArrayList<Map<String,Object>> dataRetrieved=(ArrayList<Map<String,Object>>)super.element.getElementAttributes().get("reports");
		ObjectMapper mapper=new ObjectMapper();
		ArrayList<Report> reports=mapper.convertValue(super.element.getElementAttributes().get("reports"),
				new TypeReference<ArrayList<Report>>() {});
		if (!super.element.getElementAttributes().containsKey("reportsArchive"))
			super.element.getElementAttributes().put("reportsArchive", new ArrayList<Report>());
		ArrayList<Report> reportsInArchive=(ArrayList<Report>)super.element.getElementAttributes().get("reportsArchive");
		for (int i=0;i<reports.size();i++)
			reportsInArchive.add(reports.get(i));
		LinkedHashMap<String, Object> newReportsInArchive=new LinkedHashMap<String,Object>();
		newReportsInArchive.put("reportsArchive", reportsInArchive);
		super.elementService.updateElementAttributes(super.element.getElementId().getDomain(),
				super.element.getElementId().getId(), newReportsInArchive);
		}
		catch (Exception e) {
			
		}
		
	}

}
