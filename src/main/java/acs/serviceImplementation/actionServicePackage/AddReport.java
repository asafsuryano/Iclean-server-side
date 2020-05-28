package acs.serviceImplementation.actionServicePackage;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import acs.actionBoundaryPackage.ActionBoundary;
import acs.data.reportsAttributes.Report;
import acs.logic.ElementService;
import acs.logic.UserService;

public class AddReport extends Action{
	
	

	
	
	
	

	public AddReport(UserService userService, ElementService elementService, ActionBoundary action) {
		super(userService, elementService, action);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void invoke() throws JsonMappingException, JsonProcessingException {
		Map<String, Object> actionAttr = action.getActionAttributes();
		ObjectMapper mapper = new ObjectMapper(); 
		Report r = mapper.readValue(actionAttr.get("report").toString(),Report.class);
		Map<String, Object> elemAttr = super.element.getElementAttributes();
		if(!elemAttr.containsKey("reports"))
			elemAttr.put("reports", Collections.<Report>emptyList());
		//List<Report> lst =(List<Report>) elemAttr.get("reports");
		Optional.ofNullable(elemAttr.get("reports")).filter(List<Report>.)
		if(elemAttr.get("reports") instanceof List)
			((List)elemAttr.get("reports")).add(r);
	}
	
}
