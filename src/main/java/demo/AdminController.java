package demo;

import java.util.Date;
import java.util.HashMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import ActionBoundaryClasses.ActionBoundary;
import ActionBoundaryClasses.ActionId;
import ActionBoundaryClasses.Element;
import ActionBoundaryClasses.ElementId;
import ActionBoundaryClasses.InvokedBy;
import ActionBoundaryClasses.UserId;

@RestController
public class AdminController {
	@SuppressWarnings("serial")
	@RequestMapping(path = "acs/admin/users/{adminDomain}/{adminEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ActionBoundary exportAllUsers(@PathVariable("adminDomain") String adminDomain,
			@PathVariable("adminEmail") String adminEmail) {
	
		
		return new ActionBoundary("actionType",
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
	}
}




