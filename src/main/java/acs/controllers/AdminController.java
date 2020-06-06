package acs.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import acs.Utils.StringUtil;
import acs.actionBoundaryPackage.ActionBoundary;
import acs.logic.ElementService;
import acs.logic.ExtraActionService;
import acs.logic.ExtraUserService;
import acs.usersBoundaryPackage.UserBoundary;


@RestController
public class AdminController {
	private ElementService elementService;
	private ExtraUserService userService;
	private ExtraActionService actionService;

	public AdminController(ElementService elementService, ExtraUserService userService, ExtraActionService actionService) {
		super();
		this.elementService = elementService;
		this.userService = userService;
		this.actionService = actionService;
	}

	@Autowired
	public AdminController() {
	}

	@Autowired
	public void setAdminServices(ElementService elementService, ExtraUserService userService, ExtraActionService actionService) {
		this.elementService = elementService;
		this.userService = userService;
		this.actionService = actionService;
	}

	@RequestMapping(path = "/acs/admin/actions/{adminDomain}/{adminEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ActionBoundary[] exportAllActions(@PathVariable("adminDomain") String adminDomain,
			@PathVariable("adminEmail") String adminEmail,
			@RequestParam(name = "size", required = false, defaultValue = "4") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		if(StringUtil.isNullOrEmpty(adminEmail)|| StringUtil.isNullOrEmpty(adminDomain))
		{
			throw new RuntimeException("adminDomain or adminEmail null/empty");
		}
		return this.actionService.getAllActionsWithPagination(adminDomain, adminEmail, size, page).toArray(new ActionBoundary[0]);
	}


	@RequestMapping(path = "/acs/admin/users/{adminDomain}/{adminEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] exportAllUsers(@PathVariable("adminDomain") String adminDomain,
			@PathVariable("adminEmail") String adminEmail,
			@RequestParam(name = "size", required = false, defaultValue = "4") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		if(StringUtil.isNullOrEmpty(adminEmail)|| StringUtil.isNullOrEmpty(adminDomain))
		{
			throw new RuntimeException("adminDomain or adminEmail null/empty");
		}
		return this.userService.getAllUsersWithPagination(adminDomain, adminEmail,size,page).toArray(new UserBoundary[0]);
	}


	@RequestMapping(path = "/acs/admin/users/{adminDomain}/{adminEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllUsers (
			@PathVariable("adminDomain") String adminDomain,
			@PathVariable("adminEmail") String adminEmail) {
		if(StringUtil.isNullOrEmpty(adminEmail)|| StringUtil.isNullOrEmpty(adminDomain))
		{
			throw new RuntimeException("adminDomain or adminEmail null/empty");
		}
		this.userService.deleteAllUsers(adminDomain, adminEmail);
	}

	@RequestMapping(path = "/acs/admin/elements/{adminDomain}/{adminEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllElements (
			@PathVariable("adminDomain") String adminDomain,
			@PathVariable("adminEmail") String adminEmail) {
		if(StringUtil.isNullOrEmpty(adminEmail)|| StringUtil.isNullOrEmpty(adminDomain))
		{
			throw new RuntimeException("adminDomain or adminEmail null/empty");
		}
		this.elementService.deleteAllElements(adminDomain, adminEmail);
	}

	@RequestMapping(path = "/acs/admin/actions/{adminDomain}/{adminEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllActions (
			@PathVariable("adminDomain") String adminDomain,
			@PathVariable("adminEmail") String adminEmail) {
		if(StringUtil.isNullOrEmpty(adminEmail)|| StringUtil.isNullOrEmpty(adminDomain))
		{
			throw new RuntimeException("adminDomain or adminEmail null/empty");
		}
		this.actionService.deleteAllActions(adminDomain, adminEmail);
	}


}




