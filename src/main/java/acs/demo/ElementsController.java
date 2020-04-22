package acs.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import acs.Utils.StringUtil;
import acs.elementBoundaryPackage.ElementBoundary;
import acs.logic.ElementService;

@RestController
public class ElementsController {
	private ElementService elementService;

	public ElementsController(ElementService elementService) {
		super();
		this.elementService = elementService;
	}

	@Autowired
	public ElementsController() {
	}

	@Autowired
	public void setElementService(ElementService elementService) {
		this.elementService = elementService;
	}

	@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}/{elementDomain}/{elementID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary retreiveSpecificElement(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail, @PathVariable("elementDomain") String elementDomain,
			@PathVariable("elementID") 	String elementID) {
		if(StringUtil.isNullOrEmpty(userEmail)|| 
				StringUtil.isNullOrEmpty(elementID) ||
				StringUtil.isNullOrEmpty(elementDomain)|| 
				StringUtil.isNullOrEmpty(userDomain))
		{
			throw new RuntimeException("userEmail,elementID,elementDomain,userDomain null/empty");
		}
		return this.elementService.getSpecificElement(userDomain, userEmail, elementDomain, elementID);

	}
	@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getAllelements(
			@RequestBody @PathVariable("userDomain") String userDomain, @PathVariable("userEmail") String userEmail){
		if(StringUtil.isNullOrEmpty(userDomain)|| StringUtil.isNullOrEmpty(userEmail))
		{
			throw new RuntimeException("userDomain or userEmail null/empty");
		}
		return this.elementService.getAll(userDomain, userEmail).toArray(new ElementBoundary[0]);

	}
	@RequestMapping(path = "/acs/elements/{managerDomain}/{managerEmail}",
			method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary createNewElement(@RequestBody ElementBoundary element,
			@PathVariable("managerDomain") String managerDomain,
			@PathVariable("managerEmail") String managerEmail) {
		if(StringUtil.isNullOrEmpty(managerDomain)|| StringUtil.isNullOrEmpty(managerEmail))
		{
			throw new RuntimeException("managerEmail or managerDomain null/empty");
		}
		return this.elementService.create(managerDomain, managerEmail, element);
	}


	@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}/{elementDomain}/{elementID}", 
			method = RequestMethod.PUT, 
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateElement(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail, @PathVariable("elementDomain") String elementDomain,
			@PathVariable("elementID") String elementID,
			@RequestBody ElementBoundary element)
	{
		if(StringUtil.isNullOrEmpty(userEmail)|| 
				StringUtil.isNullOrEmpty(elementID) ||
				StringUtil.isNullOrEmpty(elementDomain)|| 
				StringUtil.isNullOrEmpty(userDomain))
		{
			throw new RuntimeException("userEmail,elementID,elementDomain,userDomain null/empty");
		}
		this.elementService.update(userDomain, userEmail, elementDomain, elementID, element);
	}
}

