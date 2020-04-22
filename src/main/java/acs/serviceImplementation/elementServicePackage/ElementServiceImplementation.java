package acs.serviceImplementation.elementServicePackage;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
	
	public ElementServiceImplementation(ElementEntityBoundaryConverter converter) {
		this.converter = converter;
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
			element.setActive(true);
		}
		
		if(element.getName() == null) {
			element.setName("");
		}
		
		element.setDate(new Date());
		element.setCreatedby(new CreatedBy(new UserId(managerDomain, managerEmail)));
		
		if(element.getElementAttributes() == null) {
			element.setElementAttributes(new LinkedHashMap<>());
		}
		
		//need to get location GPS from the client and give him also option to enter manually
		//in case he has no reception
		if(element.getLocation() == null) {
			element.setLocation(new Location(0,0));
		}
		
		
		ElementEntity el = this.converter.boundaryToEntity(element);
		
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
		if(element.isActive() == false && (update.isActive() == null || update.isActive() == false)) {
			throw new RuntimeException("Element Not Active! Cannot update...");
		}
		
		boolean dirty = false;
		
		if(update.getType() != null) {
			element.setType(update.getType());
			dirty = true;
		}
		
		if(update.isActive() != null) {
			element.setActive(update.isActive());
			dirty = true;
		}

				
		if(update.getName() != null) {
			element.setName(update.getName());
			dirty = true;
		}
		
		
		if(update.getElementAttributes() != null) {
			element.setElementAttributes(update.getElementAttributes());
			dirty = true;
		}
		
		if(update.getLocation() != null) {
			element.setLocation(new acs.data.elementEntityProperties.Location(update.getLocation().getLat(),update.getLocation().getLng()));
			dirty = true;
		}
		if(dirty) {
			this.elementsDatabase.put(id, element);
		}
		
			
		return this.converter.entityToBoundary(element);
	}

	@Override
	public List<ElementBoundary> getAll(String userDomain, String userEmail) {
		return this.elementsDatabase // Map<String, ElementEntity>
				.values()           // Collection<ElementEntity>
				.stream()		    // Stream<ElementEntity>				
				.map(this.converter::entityToBoundary)	
				.filter(el -> el.isActive() == true)
				.collect(Collectors.toList()); // List<DummyBoundaries>
	}

	@Override
	public ElementBoundary getSpecificElement(String userDomain, String userEmail, String elementDomain,
			String elementId) {
		acs.data.elementEntityProperties.ElementId id = new acs.data.elementEntityProperties.ElementId(elementDomain, elementId);
		ElementEntity elEntity =  this.elementsDatabase.get(id);
		if(elEntity != null && elEntity.isActive() == true) {
			return this.converter
					.entityToBoundary(
							elEntity);
		}
		else {
			throw new RuntimeException("Could no find Element");
		}
	}

	@Override
	public void deleteAllElements(String adminDomain, String adminEmail) {
		this.elementsDatabase.clear();
	}

}
