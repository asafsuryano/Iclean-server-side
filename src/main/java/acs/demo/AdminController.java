package acs.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import acs.actionBoundaryPackage.ActionBoundary;
import acs.logic.ActionService;
import acs.logic.ElementService;
import acs.logic.UserService;
import acs.usersBoundaryPackage.UserBoundary;

@RestController
public class AdminController {
	private ElementService elementService;
	private UserService userService;
	private ActionService actionService;
	
	public AdminController(ElementService elementService, UserService userService, ActionService actionService) {
		super();
		this.elementService = elementService;
		this.userService = userService;
		this.actionService = actionService;
	}

	@Autowired
	public AdminController() {
	}
	
	@Autowired
	public void setAdminServices(ElementService elementService, UserService userService, ActionService actionService) {
		this.elementService = elementService;
		this.userService = userService;
		this.actionService = actionService;
	}

	@RequestMapping(path = "acs/admin/actions/{adminDomain}/{adminEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ActionBoundary[] exportAllActions(@PathVariable("adminDomain") String adminDomain,
			@PathVariable("adminEmail") String adminEmail) {
	
		return this.actionService.getAllActions(adminDomain, adminEmail).toArray(new ActionBoundary[0]);
	}
	
	
	@RequestMapping(path = "acs/admin/users/{adminDomain}/{adminEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] exportAllUsers(@PathVariable("adminDomain") String adminDomain,
			@PathVariable("adminEmail") String adminEmail) {
		    
		   return this.userService.getAllUsers(adminDomain, adminEmail).toArray(new UserBoundary[0]);
			}
	
	
	@RequestMapping(path = "acs/admin/users/{adminDomain}/{adminEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllUsers (
			@PathVariable("adminDomain") String adminDomain,
			@PathVariable("adminEmail") String adminEmail) {
		this.userService.deleteAllUsers(adminDomain, adminEmail);
	}
	
	@RequestMapping(path = "acs/admin/elements/{adminDomain}/{adminEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllElements (
			@PathVariable("adminDomain") String adminDomain,
			@PathVariable("adminEmail") String adminEmail) {
		this.elementService.deleteAllElements(adminDomain, adminEmail);
	}
	
	@RequestMapping(path = "acs/admin/actions/{adminDomain}/{adminEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllActions (
			@PathVariable("adminDomain") String adminDomain,
			@PathVariable("adminEmail") String adminEmail) {
		this.actionService.deleteAllActions(adminDomain, adminEmail);
	}
	
	
}




