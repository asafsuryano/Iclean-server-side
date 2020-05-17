package acs.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import acs.actionBoundaryPackage.ActionBoundary;
import acs.logic.ExtraActionService;
import acs.logic.ExtraUserService;


@RestController
public class ActionController {
	
	private ExtraActionService actionService;
	private ExtraUserService userService;
	
	
	public ActionController(ExtraActionService actionService) {
		super();
		this.actionService = actionService;
	}

	@Autowired
	public ActionController() {
	}
	
	@Autowired
	public void setActionService(ExtraActionService actionService) {
		this.actionService = actionService;
	}
	@Autowired
	public void setUserService(ExtraUserService userService) {
	 this.userService=userService;
	}
	
	@RequestMapping(path="/acs/actions",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public Object invokeAction(@RequestBody  ActionBoundary action) {
		return this.actionService.invokeAction(action);
	}
}
