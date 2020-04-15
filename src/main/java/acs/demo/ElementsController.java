package acs.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import acs.elementBoundaryPackage.ElementBoundary;
import acs.elementBoundaryPackage.ElementId;

@RestController
public class ElementsController {
	
	@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}/{elementDomain}/{elementID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary retreiveSpecificElement(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail, @PathVariable("elementDomain") String elementDomain,
			@PathVariable("elementID") 	String elementID) {
		return new ElementBoundary(userDomain, userEmail, elementDomain, elementID);
		
	}
	@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getAllelements(
		    @RequestBody @PathVariable("userDomain") String userDomain, @PathVariable("userEmail") String userEmail){
		Random rand = new Random(System.currentTimeMillis());
		int count=4;
		return 
		     IntStream.range(0, count).// Stream of Integer
		     mapToObj(i->retreiveSpecificElement(userDomain, userEmail,"goolge", Integer.toString(rand.nextInt(10))))// Stream of ElementBoundary
		     .map(element->{
		    	 Map<String, Object> elementsAttribute = new HashMap<>();
					elementsAttribute.putAll(element.getElementAttribute());
					elementsAttribute.put("Level of dirt", rand.nextInt(10));
					element.setElementAttrbiutes(elementsAttribute);
					return element;
		     })
		     .collect(Collectors.toList())    //list ElementBoundary
		     .toArray(new ElementBoundary[0]);// ElementsBoundary []
		   
		    }
		@RequestMapping(path = "/acs/elements/{managerDomain}/{managerEmail}",
				method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE,
				consumes = MediaType.APPLICATION_JSON_VALUE)
		public ElementBoundary createNewElement(@RequestBody ElementBoundary element,
				@PathVariable("managerDomain") String managerDomain,
				@PathVariable("managerEmail") String managerEmail) {
			element.setElementId(new ElementId(managerDomain, "1213"));
			return element;
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
			return;
		}
   	}

