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
		ObjectMapper mapper = new ObjectMapper(); 
		Report r = mapper.readValue(actionAttr.get("report").toString(),Report.class);
		r.setUserId(action.getInvokedBy().getUserId().toString());
		r.setCreatedTimeStamp(new Date());
		Map<String, Object> elemAttr = super.element.getElementAttributes();
		if(!elemAttr.containsKey("reports"))
			elemAttr.put("reports", Collections.<Report>emptyList());
		ArrayList<Report> lst = (ArrayList<Report>)elemAttr.get("reports");
		lst.add(r);
		super.elementService.updateElementAttributes(super.element.getElementId().getDomain(),
				super.element.getElementId().getId(), elemAttr);
		}
		catch(Exception ex) {
			throw new RuntimeException("action invoke failed -"+ex.getMessage());
		}
		
	}
	
}
