package acs.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import acs.actionBoundaryPackage.ActionBoundary;
import acs.logic.ActionService;


@RestController
public class ActionController {
	
	private ActionService actionService;
	
	public ActionController(ActionService actionService) {
		super();
		this.actionService = actionService;
	}

	@Autowired
	public ActionController() {
	}
	
	@Autowired
	public void setActionService(ActionService actionService) {
		this.actionService = actionService;
	}
	
	@RequestMapping(path="/acs/actions",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public Object invokeAction(@RequestBody  ActionBoundary action) {
		return this.actionService.invokeAction(action);
	}
}
