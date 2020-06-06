package acs.controllers;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import acs.Utils.StringUtil;
import acs.elementBoundaryPackage.ElementBoundary;
import acs.elementBoundaryPackage.ElementIdBoundary;
import acs.logic.ExtraElementsService;
import acs.logic.UserService;
import acs.serviceImplementation.elementServicePackage.ElementIsNotActiveException;


@RestController
public class ElementsController {
	private ExtraElementsService elementService;
	
	@Autowired
	public ElementsController(ExtraElementsService elementService) {
		this.elementService = elementService;
	}
	

	public ElementsController(ExtraElementsService elementService,UserService userService ) {
		super();
		this.elementService = elementService;
	}
	@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}/{elementDomain}/{elementID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary retreiveSpecificElement(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail, @PathVariable("elementDomain") String elementDomain,
			@PathVariable("elementID") String elementID) {
		if (StringUtil.isNullOrEmpty(userEmail) || StringUtil.isNullOrEmpty(elementID)
				|| StringUtil.isNullOrEmpty(elementDomain) || StringUtil.isNullOrEmpty(userDomain)) {
			throw new RuntimeException("userEmail,elementID,elementDomain,userDomain null/empty");
		}
		return this.elementService.getSpecificElement(userDomain, userEmail, elementDomain, elementID);

	}

	@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getAllelements(@RequestBody @PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail,
			@RequestParam(name = "size", required = false, defaultValue = "4") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		if (StringUtil.isNullOrEmpty(userDomain) || StringUtil.isNullOrEmpty(userEmail)) {
			throw new RuntimeException("userDomain or userEmail null/empty");
		}
		return this.elementService.getAllElementsWithPagination(userDomain,userEmail,size, page);

	}

	@RequestMapping(path = "/acs/elements/{managerDomain}/{managerEmail}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary createNewElement(@RequestBody ElementBoundary element,
			@PathVariable("managerDomain") String managerDomain, @PathVariable("managerEmail") String managerEmail) {
		if (StringUtil.isNullOrEmpty(managerDomain) || StringUtil.isNullOrEmpty(managerEmail)) {
			throw new RuntimeException("managerEmail or managerDomain null/empty");
		}
		if (StringUtil.isNullOrEmpty(element.getName()) || StringUtil.isNullOrEmpty(element.getType()))
			throw new RuntimeException("empty name or type");
		return this.elementService.create(managerDomain, managerEmail, element);
	}

	@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}/{elementDomain}/{elementID}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateElement(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail, @PathVariable("elementDomain") String elementDomain,
			@PathVariable("elementID") String elementID, @RequestBody ElementBoundary element) {
		if (StringUtil.isNullOrEmpty(userEmail) || StringUtil.isNullOrEmpty(elementID)
				|| StringUtil.isNullOrEmpty(elementDomain) || StringUtil.isNullOrEmpty(userDomain)) {
			throw new RuntimeException("userEmail,elementID,elementDomain,userDomain null/empty");
		}
		this.elementService.update(userDomain, userEmail, elementDomain, elementID, element);
	}

	@RequestMapping(path = "/acs/elements/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}/children", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void bindExistingElementToChildElement(@PathVariable("managerDomain") String managerDomain,
			@PathVariable("managerEmail") String managerEmail, @PathVariable("elementDomain") String elementDomain,
			@PathVariable("elementId") String elementID, @RequestBody ElementIdBoundary element) {
		if (StringUtil.isNullOrEmpty(managerEmail) || StringUtil.isNullOrEmpty(elementID)
				|| StringUtil.isNullOrEmpty(elementDomain) || StringUtil.isNullOrEmpty(managerDomain)) {
			throw new RuntimeException("userEmail,elementID,elementDomain,userDomain null/empty");
		}
		// TODO
		ElementBoundary parent = this.elementService.getSpecificElement(managerDomain, managerEmail, elementDomain,
				elementID);
		ElementBoundary child = this.elementService.getSpecificElement(managerDomain, managerEmail, element.getDomain(),
				element.getId());

		this.elementService.bindParentToChildElements(parent, child);
	}

	@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}/{elementDomain}/{elementId}/children", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getAllChildrenOfExistingElement(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail, @PathVariable("elementDomain") String elementDomain,
			@PathVariable("elementId") String elementId,
			@RequestParam(name = "size", required = false, defaultValue = "4") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		if (StringUtil.isNullOrEmpty(userEmail) || StringUtil.isNullOrEmpty(elementId)
				|| StringUtil.isNullOrEmpty(elementDomain) || StringUtil.isNullOrEmpty(userDomain)) {
			throw new RuntimeException("userEmail,elementID,elementDomain,userDomain null/empty");
		}
		return this.elementService.getChildrenElements(userDomain,userEmail,elementDomain, elementId, size, page);
	}

	@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}/{elementDomain}/{elementID}/parents",
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getAllParentsOfElement(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail, @PathVariable("elementDomain") String elementDomain,
			@PathVariable("elementID") String elementID,
			@RequestParam(name = "size", required = false, defaultValue = "4") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		if (StringUtil.isNullOrEmpty(userEmail) || StringUtil.isNullOrEmpty(elementID)
				|| StringUtil.isNullOrEmpty(elementDomain) || StringUtil.isNullOrEmpty(userDomain)) {
			throw new RuntimeException("userEmail,elementID,elementDomain,userDomain null/empty");
		}
		return this.elementService.getAllParentsOfElement(userDomain,userEmail,elementDomain, elementID, size, page);
	}

	@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}/search/byName/{name}",
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] searchElementsByName(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail, @PathVariable("name") String name,
			@RequestParam(name = "size", required = false, defaultValue = "4") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		if (StringUtil.isNullOrEmpty(name) || StringUtil.isNullOrEmpty(userEmail)
				|| StringUtil.isNullOrEmpty(userDomain)) {
			throw new RuntimeException("invalid url");
		}
		return this.elementService.getElementsWithSpecificNameWithPagination(userDomain,userEmail,name, size, page)
				.toArray(new ElementBoundary[0]);
	}

	@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}/search/byType/{type}",
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] searchElementsByType(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail, @PathVariable("type") String type,
			@RequestParam(name = "size", required = false, defaultValue = "4") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		if (StringUtil.isNullOrEmpty(type) || StringUtil.isNullOrEmpty(userEmail)
				|| StringUtil.isNullOrEmpty(userDomain)) {
			throw new RuntimeException("invalid url");
		}
		return this.elementService.getElementsWithSpecificTypeWithPagination(userDomain,userEmail,type, size, page)
				.toArray(new ElementBoundary[0]);
	}
    
	
	
	@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}/search/near/{lat}/{lng}/{distance}",
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] searchElementsByLocation(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail, @PathVariable("lat") double lat,
			@PathVariable("lng") double lng, @PathVariable("distance") double distance,
			@RequestParam(name = "size", required = false, defaultValue = "4") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		if (Double.isNaN(lat)||Double.isNaN(lng)||Double.isNaN(distance) || StringUtil.isNullOrEmpty(userEmail)
				|| StringUtil.isNullOrEmpty(userDomain)) {
			throw new RuntimeException("invalid url");
		}
		return this.elementService.getElementsNearWithPagination(userDomain,userEmail,lat, lng, distance, size, page)
				.toArray(new ElementBoundary[0]);
		}
	
	
	@ExceptionHandler
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public Map<String, Object> handleException (ElementIsNotActiveException e){
		return Collections.singletonMap("error", 
				(e.getMessage() == null)?
						"Element is not active":
						e.getMessage());
	}
}
