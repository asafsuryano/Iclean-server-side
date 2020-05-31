package acs.serviceImplementation.actionServicePackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Map;


import com.fasterxml.jackson.databind.ObjectMapper;

import acs.actionBoundaryPackage.ActionBoundary;
import acs.data.reportsAttributes.Report;
import acs.logic.ExtraElementsService;
import acs.logic.UserService;

public class AddReport extends Action{
	
	

	public AddReport(UserService userService, ExtraElementsService elementService, ActionBoundary action) {
		super(userService, elementService, action);
	}

	@Override
	public void invoke(){
		try {
		Map<String, Object> actionAttr = action.getActionAttributes();
		Report r = new Report((Map)actionAttr.get("report"));
		Report r2=new Report(3, "asaf is the best",
				new Date(),action.getInvokedBy().getUserId().toString());
		r.setUserId(action.getInvokedBy().getUserId().toString());
		r.setCreatedTimeStamp(new Date());
		super.init();
		Map<String, Object> elemAttr = super.element.getElementAttributes();
		if(!elemAttr.containsKey("reports"))
			elemAttr.put("reports", new ArrayList<Report>());
		ArrayList<Report> lst = (ArrayList<Report>)elemAttr.get("reports");
		lst.add(r);
		lst.add(r2);
		super.elementService.updateElementAttributes(super.element.getElementId().getDomain(),
				super.element.getElementId().getId(), elemAttr);
		}
		catch(Exception ex) {
			throw new RuntimeException("action invoke failed -"+ex.getMessage());
		}
		
	}
	
}
