package acs.logic;

import java.util.List;

import acs.usersBoundaryPackage.UserBoundary;

public interface ExtraUserService extends UserService{
	public List<UserBoundary> getAllUsersWithPagination(String adminDomain, String adminEmail,int size,int page);

}
