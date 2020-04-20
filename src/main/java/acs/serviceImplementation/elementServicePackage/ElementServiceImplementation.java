package acs.serviceImplementation.elementServicePackage;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import acs.data.ElementEntity;
import acs.data.ElementEntityBoundaryConverter;
import acs.elementBoundaryPackage.CreatedBy;
import acs.elementBoundaryPackage.ElementBoundary;
import acs.elementBoundaryPackage.ElementId;
import acs.elementBoundaryPackage.Location;
import acs.elementBoundaryPackage.UserId;
import acs.logic.ElementService;


@Service
public class ElementServiceImplementation implements ElementService {
	private String projectName;
	private Map<acs.data.elementEntityProperties.ElementId, ElementEntity> elementsDatabase;
	private ElementEntityBoundaryConverter converter; 
	
	public ElementServiceImplementation() {
		// TODO Auto-generated constructor stub
		this.converter =  new ElementEntityBoundaryConverter();
	}

	@PostConstruct
	public void init() {
		// since this class is a singleton, we generate a thread safe collection
		this.elementsDatabase = Collections.synchronizedMap(new HashMap<>());
	}
	
	// injection of value from the spring boot configuration
	@Value("${spring.application.name:demo}")
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	public ElementBoundary create(String managerDomain, String managerEmail, ElementBoundary element) {
		ElementId elementId = new ElementId(this.projectName, UUID.randomUUID().toString());
		element.setElementId(elementId);
		if(element.getType() == null) {
			element.setType("");
		}
		if(element.isActive() == null) {
			element.setActive(false);
		}
		
		if(element.getName() == null) {
			element.setName("");
		}
		
		element.setDate(new Date());
		element.setCreatedby(new CreatedBy(new UserId(managerDomain, managerEmail)));
		
		if(element.getElementAttribute() == null) {
			element.setElementAttrbiutes(new HashMap<>());
		}
		
		if(element.getLocation() == null) {
			element.setLocation(new Location(0,0));
		}
		
		ElementEntity el = this.converter.fromBoundarytoEntity(element);
		
		this.elementsDatabase.put(el.getElementId(), el);
		
		return element;
	}

	@Override
	public ElementBoundary update(String managerDomain, String managerEmail, String elementDomain, String elementId,
			ElementBoundary update) {
		
		acs.data.elementEntityProperties.ElementId id = new acs.data.elementEntityProperties.ElementId(elementDomain, elementId);
		ElementEntity element = this.elementsDatabase.get(id);
		if(element == null) {
			throw new RuntimeException("Invalid Element");
		}
		if(element.isActive() == false) {
			throw new RuntimeException("Element Not Active! Cannot update...");
		}
		
		if(update.getType() == null) {
			
		}
		else {
			element.setType(update.getType());
		}
				
		if(update.getName() == null) {
			
		}
		else {
			element.setName(update.getName());
		}
		
//		update.setDate(new Date());
		
		if(update.getElementAttribute() == null) {
			
		}
		else {
			element.setElementAttributes(update.getElementAttribute());
		}
		
		if(update.getLocation() == null) {
			//leave element location as it was created
		}
		else {
			element.setLocation(new acs.data.elementEntityProperties.Location(update.getLocation().getLat(),update.getLocation().getIng()));
		}
			
		return this.converter.fromEntityToBoundary(element);
	}

	@Override
	public List<ElementBoundary> getAll(String userDomain, String userEmail) {
		// TODO Auto-generated method stub
		return this.elementsDatabase // Map<String, ElementEntity>
				.values()           // Collection<ElementEntity>
				.stream()		    // Stream<ElementEntity>				
				.map(this.converter::fromEntityToBoundary)	
				.filter(el -> el.isActive() == true)
				.collect(Collectors.toList()); // List<DummyBoundaries>
	}

	@Override
	public ElementBoundary getSpecificElement(String userDomain, String userEmail, String elementDomain,
			String elementId) {
		// TODO Auto-generated method stub
		acs.data.elementEntityProperties.ElementId id = new acs.data.elementEntityProperties.ElementId(elementDomain, elementId);
		ElementEntity elEntity =  this.elementsDatabase.get(id);
		if(elEntity != null) {
			return this.converter
					.fromEntityToBoundary(
							elEntity);
		}
		else {
			throw new RuntimeException("Could no find Element");
		}
	}

	@Override
	public void deleteAllElements(String userDomain, String adminEmail) {
		// TODO Auto-generated method stub
		this.elementsDatabase.clear();
	}

}
