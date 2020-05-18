package acs.data;

import acs.elementBoundaryPackage.*;

import org.springframework.stereotype.Component;

import acs.data.elementEntityProperties.ElementId;
import acs.data.elementEntityProperties.Location;
import acs.data.elementEntityProperties.UserId;

@Component
public class ElementEntityBoundaryConverter {
	public ElementBoundary entityToBoundary(ElementEntity entity) {
		ElementBoundary eb=new ElementBoundary();
		eb.setElementId(new acs.elementBoundaryPackage.ElementIdBoundary(entity.getElementId().getElementDomain(),
				entity.getElementId().getElementId()));
		eb.setLocation(new acs.elementBoundaryPackage.Location(entity.getLocation().getLat(),
				entity.getLocation().getLng()));
		eb.setCreatedTimestamp(entity.getCreatedTimeStamp());
		eb.setActive(entity.isActive());
		eb.setCreatedBy(new CreatedBy(new acs.elementBoundaryPackage.UserId(entity.getCreatedBy().getDomain(),
				entity.getCreatedBy().getEmail())));
		eb.setElementAttributes(entity.getElementAttributes());
		eb.setType(entity.getType().toString());
		eb.setName(entity.getName());
		return eb;
	}
	public ElementEntity boundaryToEntity(ElementBoundary boundary) {
		ElementEntity entity=new ElementEntity();
		entity.setElementId(new ElementId(boundary.getElementId().getDomain(),
				boundary.getElementId().getId()));
		entity.setName(boundary.getName());
		entity.setLocation(new Location(boundary.getLocation().getLat(), boundary.getLocation().getLng()));
		entity.setCreatedTimeStamp(boundary.getCreatedTimestamp());
		
		entity.setType(boundary.getType());
		
		entity.setCreatedBy(new UserId(boundary.getCreatedBy().getUserId().getDomain(),
				boundary.getCreatedBy().getUserId().getEmail()));
		entity.setElementAttributes(boundary.getElementAttributes());
		entity.setActive(boundary.isActive());
		return entity;
	}
	
}