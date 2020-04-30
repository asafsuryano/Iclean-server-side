package acs.logic;

import java.util.Set;

import acs.elementBoundaryPackage.ElementBoundary;

public interface ExtraElementsService extends ElementService {

	public void bindParenToChildElements (String parentElement, String childId);
	public Set<ElementBoundary> getAllChildren(String originId);
}
