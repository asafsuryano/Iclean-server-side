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
import acs.logic.ExtraElementsService;

@Component
public class ActionManager {

	private String actions;
	protected UserService userService;
	protected ExtraElementsService elementService;
	protected ActionBoundary action;
	protected Map<String,String> actionsMap;

	public ActionManager(UserService userService,ExtraElementsService elementService) {
		this.userService = userService;
		this.elementService = elementService;
	}
	
	
	public void setAction(ActionBoundary action) {
		this.action = action;
	}
	
	@Value("${action.actions}")
	public void setActionsMap(String actionsMapString) {
		this.actionsMap=convertStringToMap(actionsMapString);
	}

	public Action getAction() {
		
		try {
			String className=this.actionsMap.get(this.action.getType());			
			Constructor<?> c = Class.forName(className).getConstructor(UserService.class, ExtraElementsService.class,ActionBoundary.class);
			return (Action) c.newInstance(userService, elementService, action);
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
		    map.put(trimValue(entry[0]), trimValue(entry[1]));          //add them to the hashmap and trim whitespaces
		}
		return map;
	}
	
	private String trimValue(String str) {
		String trimmed=str.trim();
		return trimmed.substring(1, trimmed.length()-1);
	}

}
