package acs.serviceImplementation.actionServicePackage;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import acs.actionBoundaryPackage.ActionBoundary;
import acs.data.reportsAttributes.Report;
import acs.logic.ElementService;
import acs.logic.UserService;

@Component
public class ActionManager {

	//@Value("#{'${action.actions}'.split(',')}") 
	@Value("#{action.actions}")
	//@Value("${spring.application.name:demo}")
	//@Value("#{${action.actions}}")
	private String actions;
	protected UserService userService;
	protected ElementService elementService;
	protected ActionBoundary action;

	public ActionManager(UserService userService,ElementService elementService) {
		this.userService = userService;
		this.elementService = elementService;
//		this.action = action;
	}
	
	public void setAction(ActionBoundary action) {
		this.action = action;
	}

	public Action getAction() {
		
		try {
//			actions.stream().filter(actionClass -> actionClass.equals(type))
//			ObjectMapper mapper = new ObjectMapper(); 
//			ArrayList<String> r = mapper.readValue(actions,ArrayList<String>.class);
			String className=convertStringToMap(actions).get(this.action.getType());
			//return (Action) Class.forName(className).newInstance(); 
			
			Constructor<?> c = Class.forName(className).getConstructor(UserService.class, ElementService.class,ActionBoundary.class);
			return (Action) c.newInstance(userService, elementService, action);
			//return Action.class.getDeclaredConstructor(String.class).newInstance( userService, elementService, action);
		} catch (Exception e) {
			throw new RuntimeException("the action type is invalid");
		}
		
	}
	
	private Map<String,String> convertStringToMap(String str){
		str = str.substring(1, str.length()-1); 
		String[] keyValuePairs = str.split(",");   
		Map<String,String> map = new HashMap<>(); 
		for(String pair : keyValuePairs)                        //iterate over the pairs
		{
		    String[] entry = pair.split(":");                   //split the pairs to get key and value 
		    map.put(entry[0].trim(), entry[1].trim());          //add them to the hashmap and trim whitespaces
		}
		return map;
	}

}
