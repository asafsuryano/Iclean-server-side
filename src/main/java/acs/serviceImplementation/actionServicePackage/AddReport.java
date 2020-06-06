package acs.serviceImplementation.actionServicePackage;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import acs.actionBoundaryPackage.ActionBoundary;
import acs.data.reportsAttributes.Report;
import acs.logic.ExtraElementsService;
import acs.logic.UserService;

public class AddReport extends Action {

	public AddReport(UserService userService, ExtraElementsService elementService, ActionBoundary action) {
		super(userService, elementService, action);
	}

	@Override
	public void invoke() {
		try {
			Map<String, Object> actionAttr = action.getActionAttributes();
			Report r = new Report((Map) actionAttr.get("report"));
			r.setUserId(action.getInvokedBy().getUserId().toString());
			r.setCreatedTimeStamp(new Date());
			super.init();
			if (!super.element.getType().contains("location"))
				throw new RuntimeException("you can only add a report to a location element");
			Map<String, Object> elemAttr = super.element.getElementAttributes();

			if (!elemAttr.containsKey("reports"))
				elemAttr.put("reports", new ArrayList<Report>());
			ArrayList<Report> lst = (ArrayList<Report>) elemAttr.get("reports");
			if (lst.size() < 10) {
				lst.add(r);
				super.elementService.updateElementAttributes(super.element.getElementId().getDomain(),
						super.element.getElementId().getId(), elemAttr);
			}else {
				throw new RuntimeException("not more then 10 reports can be added at a time");
			}
		} catch (Exception ex) {
			throw new RuntimeException("action invoke failed -" + ex.getMessage());
		}

	}

}
