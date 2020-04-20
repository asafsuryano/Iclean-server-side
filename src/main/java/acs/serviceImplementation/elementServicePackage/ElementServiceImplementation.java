package acs.serviceImplementation.elementServicePackage;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import acs.data.ElementEntity;
import acs.data.ElementEntityBoundaryConverter;
import acs.elementBoundaryPackage.CreatedBy;
import acs.elementBoundaryPackage.ElementBoundary;
import acs.elementBoundaryPackage.ElementId;
import acs.elementBoundaryPackage.Location;
import acs.elementBoundaryPackage.Type;
import acs.elementBoundaryPackage.UserId;
import acs.logic.ElementService;


@Service
public class ElementServiceImplementation implements ElementService {

	private Map<ElementId, ElementEntity> elementsDatabase;
	private ElementEntityBoundaryConverter converter; 
	

	@PostConstruct
	public void init() {
		// since this class is a singleton, we generate a thread safe collection
		this.elementsDatabase = Collections.synchronizedMap(new HashMap<>());
	}

	@Override
	public ElementBoundary create(String managerDomain, String managerEmail, ElementBoundary element) {
		ElementId elementId = new ElementId(managerDomain, UUID.randomUUID().toString());
		element.setElementId(elementId);
		if(element.getType() == null) {
			element.setType(Type.DEMO_ELEMENT);
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
		
		this.elementsDatabase.put(element.getElementId(), this.converter.toEntity(element));
		
		return element;
	}

	@Override
	public ElementBoundary update(String managerDomain, String managerEmail, String elementDomain, String elementId,
			ElementBoundary update) {
		
		ElementId id = new ElementId(elementDomain, elementId);
		ElementEntity element = this.elementsDatabase.get(id);
		if(element == null) {
			throw new RuntimeException("Invalid Element");
		}
		
		if(update.getType() == null) {
			element.setType(Type.DEMO_ELEMENT);
		}
		if(update.isActive() == null) {
			element.setActive(false);
		}
		
		if(update.getName() == null) {
			element.setName("");
		}
		
//		update.setDate(new Date());
		
		if(update.getElementAttribute() == null) {
			element.setElementAttributes(new HashMap<>());
		}
		
		if(update.getLocation() == null) {
			//leave element location as it was created
		}
			
		return this.converter.fromEntity(element);
	}

	@Override
	public List<ElementBoundary> getAll(String userDomain, String userEmail) {
		// TODO Auto-generated method stub
		return this.elementsDatabase // Map<String, ElementEntity>
				.values()           // Collection<ElementEntity>
				.stream()		    // Stream<ElementEntity>				
				.map(this.converter::fromEntity)								
				.collect(Collectors.toList()); // List<DummyBoundaries>
	}

	@Override
	public ElementBoundary getSpecificElement(String userDomain, String userEmail, String elementDomain,
			String elementId) {
		// TODO Auto-generated method stub
		ElementId id = new ElementId(elementDomain, elementId);
		ElementEntity elEntity =  this.elementsDatabase.get(id);
		if(elEntity != null) {
			return this.converter
					.fromEntity(
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
