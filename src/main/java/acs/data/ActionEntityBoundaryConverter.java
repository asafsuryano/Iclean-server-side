package acs.data;

import acs.actionBoundaryPackage.ActionBoundary;

import acs.actionBoundaryPackage.ActionId;
import acs.actionBoundaryPackage.Element;
import acs.actionBoundaryPackage.ElementId;
import acs.actionBoundaryPackage.InvokedBy;
import acs.actionBoundaryPackage.UserId;


public class ActionEntityBoundaryConverter {
	
	public ActionBoundary entityToBoundary(ActionEntity entity) {
		ActionBoundary ab = new ActionBoundary();
		ab.setType(entity.getType());
		ab.setCreatedTimestamp(entity.getCreatedTimestamp());
		ab.setActionId(new ActionId(entity.getActionId().getDomain(), entity.getActionId().getId()));
		ab.setElement(new Element(new ElementId(entity.getElement().getElementId().getDomain(), entity.getElement().getElementId().getId())));		
		ab.setInvokedBy(new InvokedBy(new UserId(entity.getInvokedBy().getUserId().getDomain(), entity.getInvokedBy().getUserId().getEmail())));	
		ab.setActionAttributes(entity.getActionAttributes());
		return ab;
	}
	
	public ActionEntity boundaryToEntity(ActionBoundary boundary) {
		ActionEntity ae = new ActionEntity();
		ae.setType(boundary.getType());
		ae.setCreatedTimestamp(boundary.getCreatedTimestamp());
		ae.setActionId(new acs.data.actionEntityProperties.ActionId(boundary.getActionId().getDomain(), boundary.getActionId().getId()));
		ae.setElement(new acs.data.actionEntityProperties.Element(new acs.data.actionEntityProperties.ElementId(boundary.getElement().getElementId().getDomain(), boundary.getElement().getElementId().getId())));		
		ae.setInvokedBy(new acs.data.actionEntityProperties.InvokedBy(new acs.data.actionEntityProperties.UserId(boundary.getInvokedBy().getUserId().getDomain(), boundary.getInvokedBy().getUserId().getEmail())));	
		ae.setActionAttributes(boundary.getActionAttributes());
		return ae;
	}
}
