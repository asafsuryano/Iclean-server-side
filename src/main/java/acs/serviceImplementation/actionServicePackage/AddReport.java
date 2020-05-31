package acs.serviceImplementation.actionServicePackage;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

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
		r.setUserId(action.getInvokedBy().getUserId().toString());
		r.setCreatedTimeStamp(new Date());
		super.init();
		Map<String, Object> elemAttr = super.element.getElementAttributes();
		if(!elemAttr.containsKey("reports"))
			elemAttr.put("reports", new ArrayList<Report>());
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
