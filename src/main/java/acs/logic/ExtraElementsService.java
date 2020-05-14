package acs.logic;

import java.util.Set;

import acs.elementBoundaryPackage.ElementBoundary;

public interface ExtraElementsService extends ElementService {

	public void bindParentToChildElements (ElementBoundary parentElement, ElementBoundary childElement);
	public Set<ElementBoundary> getAllChildren(ElementBoundary originId);
	public ElementBoundary[] getAllElementsWithPagination(int size, int page,boolean isManager);
	public 	ElementBoundary[] getChildrenElements(String parentDomain,String parentId,int size,int page,boolean isManager);
	public ElementBoundary[] getAllParentsOfElement(String childDomain,String childId,int size,int page,boolean isManager);
}