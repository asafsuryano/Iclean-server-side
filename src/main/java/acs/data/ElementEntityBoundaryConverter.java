package acs.data;

import acs.elementBoundaryPackage.*;
import acs.data.elementEntityProperties.ElementId;
import acs.data.elementEntityProperties.Location;
import acs.data.elementEntityProperties.Type;
import acs.data.elementEntityProperties.UserId;
public class ElementEntityBoundaryConverter {
	public ElementBoundary fromEntityToBoundary(ElementEntity entity) {
		ElementBoundary eb=new ElementBoundary();
		eb.setElementId(new acs.elementBoundaryPackage.ElementId(entity.getElementId().getElementDomain(),
				entity.getElementId().getElementId()));
		eb.setLocation(new acs.elementBoundaryPackage.Location(entity.getLocation().getLat(),
				entity.getLocation().getIng()));
		eb.setDate(entity.getCreatedTimeStamp());
		eb.setActive(entity.isActive());
		eb.setCreatedby(new CreatedBy(new acs.elementBoundaryPackage.UserId(entity.getCreatedBy().getDomain(),
				entity.getCreatedBy().getEmail())));
		eb.setElementAttrbiutes(entity.getElementAttributes());
		eb.setType(entity.getType().toString());
		return eb;
	}
	public ElementEntity fromBoundarytoEntity(ElementBoundary boundary) {
		ElementEntity entity=new ElementEntity();
		entity.setElementId(new ElementId(boundary.getElementId().getElementDomain(),
				boundary.getElementId().getElementId()));
		entity.setName(boundary.getName());
		entity.setLocation(new Location(boundary.getLocation().getLat(), boundary.getLocation().getIng()));
		entity.setCreatedTimeStamp(boundary.getDate());
		entity.setType(Type.valueOf(boundary.getType()));
		entity.setCreatedBy(new UserId(boundary.getCreatedby().getUserId().getUserdomain(),
				boundary.getCreatedby().getUserId().getUserEmail()));
		entity.setElementAttributes(boundary.getElementAttribute());
		entity.setActive(boundary.isActive());
		return entity;
	}
}