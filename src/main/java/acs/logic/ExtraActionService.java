package acs.logic;

import java.util.List;

import acs.actionBoundaryPackage.ActionBoundary;

public interface ExtraActionService extends ActionService {
	public List<ActionBoundary> getAllActionsWithPagination(String adminDomain, String adminEmail,int size,int page);
}
