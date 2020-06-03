package acs.serviceImplementation.actionServicePackage;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import acs.actionBoundaryPackage.ActionBoundary;
import acs.data.reportsAttributes.Report;
import acs.data.workerAttributes.Employee;
import acs.logic.ExtraElementsService;
import acs.logic.UserService;

public class AddEmployee extends Action {

	public AddEmployee(UserService userService, ExtraElementsService elementService, ActionBoundary action) {
		super(userService, elementService, action);
		// TODO Auto-generated constructor stub
	}

	@Override
	void invoke() {
		// TODO Auto-generated method stub
		try {
		Map<String, Object> actionAttr = action.getActionAttributes();
		Employee employee = new Employee((Map)actionAttr.get("employee"));
		super.init();
		Map<String, Object> elemAttr = super.element.getElementAttributes();
		if(!elemAttr.containsKey("employees"))
			elemAttr.put("employees", new ArrayList<Employee>());
		ArrayList<Employee> lst = (ArrayList<Employee>)elemAttr.get("employees");
		lst.add(employee);
		super.elementService.updateElementAttributes(super.element.getElementId().getDomain(),
				super.element.getElementId().getId(), elemAttr);
		}
		catch(Exception ex) {
			throw new RuntimeException("action invoke failed -"+ex.getMessage());
		}
		
	}
	
	

}
