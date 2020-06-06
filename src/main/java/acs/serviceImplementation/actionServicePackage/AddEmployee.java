package acs.serviceImplementation.actionServicePackage;

import java.util.ArrayList;
import java.util.Map;

import acs.actionBoundaryPackage.ActionBoundary;
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
		if (super.element.getType().compareToIgnoreCase("shift")!=0)
			throw new RuntimeException("you can only add an employee to a shift element");
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
