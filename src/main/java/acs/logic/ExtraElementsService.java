package acs.logic;

import java.util.List;
import java.util.Set;

import acs.data.UserRoles;
import acs.elementBoundaryPackage.ElementBoundary;

public interface ExtraElementsService extends ElementService {

	public void bindParentToChildElements (ElementBoundary parentElement, ElementBoundary childElement);
	public Set<ElementBoundary> getAllChildren(ElementBoundary originId);
	public ElementBoundary[] getAllElementsWithPagination(int size, int page,boolean isManager);
	public 	ElementBoundary[] getChildrenElements(String parentDomain,String parentId,int size,int page,UserRoles role);
	public ElementBoundary[] getAllParentsOfElement(String childDomain,String childId,int size,int page,UserRoles role);
	public List<ElementBoundary> getElementsWithSpecificNameWithPagination(String name, int size,int page,UserRoles role);
	public List<ElementBoundary> getElementsWithSpecificTypeWithPagination(String Type, int size,int page,UserRoles role);
	public List<ElementBoundary> getElementsNearWithPagination(double lat,double lng,double distance, int size,int page,UserRoles role);
	public ElementBoundary getSpecificElementWithRoleChecking(String elementDomain, String elementId,UserRoles role);
}