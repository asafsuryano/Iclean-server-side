package acs.serviceImplementation.actionServicePackage;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import acs.actionBoundaryPackage.ActionBoundary;
import acs.logic.ElementService;
import acs.logic.UserService;

@Component
public class ActionManager {

	//@Value("#{'${action.actions}'.split(',')}") 
	@Value("#{${action.actions}}")
	private Map<String,String> actions;
	protected UserService userService;
	protected ElementService elementService;
	protected ActionBoundary action;

	public ActionManager(UserService userService,ElementService elementService,ActionBoundary action) {
		this.userService = userService;
		this.elementService = elementService;
		this.action = action;
	}

	public Action getAction() {
		
		try {
//			actions.stream().filter(actionClass -> actionClass.equals(type))
			String className = actions.get(this.action.getType());
			//return (Action) Class.forName(className).newInstance(); 
			
			Constructor<?> c = Class.forName(className).getConstructor(UserService.class, ElementService.class,ActionBoundary.class);
			return (Action) c.newInstance(userService, elementService, action);
			//return Action.class.getDeclaredConstructor(String.class).newInstance( userService, elementService, action);
		} catch (Exception e) {
			throw new RuntimeException("the action type is invalid");
		}
		
	}

}
