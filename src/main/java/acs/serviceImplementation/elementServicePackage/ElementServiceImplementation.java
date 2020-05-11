package acs.serviceImplementation.elementServicePackage;


import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import acs.dal.ElementDao;
import acs.data.ElementEntity;
import acs.data.ElementEntityBoundaryConverter;
import acs.data.elementEntityProperties.ElementId;
import acs.elementBoundaryPackage.CreatedBy;
import acs.elementBoundaryPackage.ElementBoundary;
import acs.elementBoundaryPackage.ElementIdBoundary;
import acs.elementBoundaryPackage.Location;
import acs.elementBoundaryPackage.UserId;
import acs.logic.ExtraElementsService;


@Service
public class ElementServiceImplementation implements ExtraElementsService {
	private String projectName;
	private ElementEntityBoundaryConverter converter; 
	private ElementDao elementDatabase;
	
	public ElementServiceImplementation(ElementDao elementDao,ElementEntityBoundaryConverter converter) {
		this.converter = converter;
		this.elementDatabase=elementDao;
	}

	/*
	@PostConstruct
	public void init() {
		// since this class is a singleton, we generate a thread safe collection
		this.elementsDatabase = Collections.synchronizedMap(new HashMap<>());
	}
	*/
	
	// injection of value from the spring boot configuration
	@Value("${spring.application.name:demo}")
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	@Transactional
	public ElementBoundary create(String managerDomain, String managerEmail, ElementBoundary element) {
		ElementIdBoundary elementId = new ElementIdBoundary(this.projectName, UUID.randomUUID().toString());
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
		element.setCreatedBy(new CreatedBy(new UserId(managerDomain, managerEmail)));
		
		if(element.getElementAttributes() == null) {
			element.setElementAttributes(new LinkedHashMap<>());
		}
		
		//need to get location GPS from the client and give him also option to enter manually
		//in case he has no reception
		if(element.getLocation() == null) {
			element.setLocation(new Location(0,0));
		}
		
		
		ElementEntity el = this.converter.boundaryToEntity(element);
		
		this.elementDatabase.save(el);
		return element;
	}

	@Override
	@Transactional
	public ElementBoundary update(String managerDomain, String managerEmail, String elementDomain, String elementId,
			ElementBoundary update) {
		
		acs.data.elementEntityProperties.ElementId id = new acs.data.elementEntityProperties.ElementId(elementDomain, elementId);
		Optional<ElementEntity> element = this.elementDatabase.findById(id);
		if(!element.isPresent()) {
			throw new RuntimeException("Invalid Element");
		}
		if(element.get().isActive() == false && (update.isActive() == null || update.isActive() == false)) {
			throw new RuntimeException("Element Not Active! Cannot update...");
		}
		
		boolean dirty = false;
		
		if(update.getType() != null) {
			element.get().setType(update.getType());
			dirty = true;
		}
		
		if(update.isActive() != null) {
			element.get().setActive(update.isActive());
			dirty = true;
		}

				
		if(update.getName() != null) {
			element.get().setName(update.getName());
			dirty = true;
		}
		
		
		if(update.getElementAttributes() != null) {
			element.get().setElementAttributes(update.getElementAttributes());
			dirty = true;
		}
		
		if(update.getLocation() != null) {
			element.get().setLocation(new acs.data.elementEntityProperties.Location(update.getLocation().getLat(),update.getLocation().getLng()));
			dirty = true;
		}
		if(dirty) {
			this.elementDatabase.save(element.get());
		}
		
			
		return this.converter.entityToBoundary(element.get());
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementBoundary> getAll(String userDomain, String userEmail) {
		return StreamSupport.stream(this.elementDatabase.findAll().spliterator(),false).
				map(this.converter::entityToBoundary).collect(Collectors.toList());

	}

	@Override
	@Transactional(readOnly = true)
	public ElementBoundary getSpecificElement(String userDomain, String userEmail, String elementDomain,
			String elementId) {
		acs.data.elementEntityProperties.ElementId id = new acs.data.elementEntityProperties.ElementId(elementDomain, elementId);
		Optional<ElementEntity> elEntity =  this.elementDatabase.findById(id);
		if (elEntity.isPresent())
		{
			if (elEntity.get().isActive())
				return this.converter.entityToBoundary(elEntity.get());
			else
				throw new RuntimeException("the element is not active");
		}
		else
			throw new RuntimeException("the element does not exist");
	}

	@Override
	public void deleteAllElements(String adminDomain, String adminEmail) {
		this.elementDatabase.deleteAll();
	}

	@Override
	public void bindParentToChildElements(ElementBoundary parentElement, ElementBoundary childElement) {
		// TODO Auto-generated method stub
		
		ElementEntity parentEntity=this.elementDatabase.findById(new ElementId
				(parentElement.getElementId().getDomain(),parentElement.getElementId().getId()))
				.orElseThrow(()->new RuntimeException("the element does not exist"));
		ElementEntity childEntity=this.elementDatabase.findById(new ElementId
				(childElement.getElementId().getDomain(),childElement.getElementId().getId()))
				.orElseThrow(()->new RuntimeException("the element does not exist"));
		childEntity.setParent(parentEntity);
		this.elementDatabase.save(childEntity);
	}

	@Override
	public Set<ElementBoundary> getAllChildren(ElementBoundary originId) {
		ElementEntity entity=this.elementDatabase.findById
				(new ElementId(originId.getElementId().getDomain(),originId.getElementId().getId()))
				.orElseThrow(()->new RuntimeException("the element does not exist"));
		return entity.getChildren().stream()
				.map(this.converter::entityToBoundary)
				.collect(Collectors.toSet());
	}


}
