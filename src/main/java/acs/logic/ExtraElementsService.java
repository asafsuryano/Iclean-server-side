package acs.logic;

import java.util.List;
import java.util.Set;

import acs.data.UserRoles;
import acs.elementBoundaryPackage.ElementBoundary;

public interface ExtraElementsService extends ElementService {

	public void bindParentToChildElements (ElementBoundary parentElement, ElementBoundary childElement);
	public Set<ElementBoundary> getAllChildren(ElementBoundary originId);
	public ElementBoundary[] getAllElementsWithPagination(String userDomain,String userEmail,int size, int page);
	public 	ElementBoundary[] getChildrenElements(String userDomain,String userEmail,String parentDomain,String parentId,int size,int page);
	public ElementBoundary[] getAllParentsOfElement(String userDomain,String userEmail,String childDomain,String childId,int size,int page);
	public List<ElementBoundary> getElementsWithSpecificNameWithPagination(String userDomain,String userEmail,String name, int size,int page);
	public List<ElementBoundary> getElementsWithSpecificTypeWithPagination(String userDomain,String userEmail,String type, int size,int page);
	public List<ElementBoundary> getElementsNearWithPagination(String userDomain,String userEmail,double lat,double lng,double distance, int size,int page);
}