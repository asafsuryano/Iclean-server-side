package acs.demo;

import java.util.Date;
import java.util.HashMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import acs.actionBoundaryPackage.ActionBoundary;
import acs.actionBoundaryPackage.ActionId;
import acs.actionBoundaryPackage.Element;
import acs.actionBoundaryPackage.ElementId;
import acs.actionBoundaryPackage.InvokedBy;
import acs.actionBoundaryPackage.UserId;
import acs.usersBoundaryPackage.Roles;
import acs.usersBoundaryPackage.User;
import acs.usersBoundaryPackage.UserBoundary;

@RestController
public class AdminController {
	@SuppressWarnings("serial")
	@RequestMapping(path = "acs/admin/actions/{adminDomain}/{adminEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ActionBoundary[] exportAllActions(@PathVariable("adminDomain") String adminDomain,
			@PathVariable("adminEmail") String adminEmail) {
	
		ActionBoundary[] arr = new ActionBoundary[1];
		arr[0]= new ActionBoundary("actionType",
				new Date(),
				new ActionId("2020b.demo","971"),
				new Element(new ElementId("2020b.demo", "54")),
				new InvokedBy(new UserId("2020b.demo", "player@de.mo")),
				new HashMap<String, Object>() {
			{
				put("key1", "can be set to any value you wish");
				put("key2", 44.5);
				put("booleanValue", false);
				put("lastKey", "can be set to any value you wish");
			}
		}
				);
		return arr; 
	}
	
	
	@RequestMapping(path = "acs/admin/users/{adminDomain}/{adminEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] exportAllUsers(@PathVariable("adminDomain") String adminDomain,
			@PathVariable("adminEmail") String adminEmail) {
		UserBoundary[] arr = new UserBoundary[1];
		arr[0] = new UserBoundary(new User("2020b.demo", "ddemo@us.er"), Roles.PLAYER.toString() ,"Demo User",";-)");
		 
		return arr;
			}
	
	
	@RequestMapping(path = "acs/admin/users/{adminDomain}/{adminEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllUsers (
			@PathVariable("adminDomain") String adminDomain,
			@PathVariable("adminEmail") String adminEmail) {
		return;//TODO
	}
	
	@RequestMapping(path = "acs/admin/elements/{adminDomain}/{adminEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllElements (
			@PathVariable("adminDomain") String adminDomain,
			@PathVariable("adminEmail") String adminEmail) {
		return;//TODO
	}
	
	@RequestMapping(path = "acs/admin/actions/{adminDomain}/{adminEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllActions (
			@PathVariable("adminDomain") String adminDomain,
			@PathVariable("adminEmail") String adminEmail) {
		return;//TODO
	}
	
	
}




