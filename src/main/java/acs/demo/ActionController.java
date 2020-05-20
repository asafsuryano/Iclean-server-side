package acs.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import acs.actionBoundaryPackage.ActionBoundary;
import acs.logic.ExtraActionService;


@RestController
public class ActionController {
	
	private ExtraActionService actionService;
	

	@Autowired
	public ActionController() {
	}
	
	@Autowired
	public void setActionService(ExtraActionService actionService) {
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
