package acs.serviceImplementation.actionServicePackage;


import acs.actionBoundaryPackage.ActionBoundary;
import acs.data.UserRoles;
import acs.elementBoundaryPackage.ElementBoundary;
import acs.logic.ExtraElementsService;
import acs.logic.UserService;
import acs.usersBoundaryPackage.UserBoundary;

public abstract class Action {
	
	
	protected UserService userService;
	protected ExtraElementsService elementService;
	protected ActionBoundary action;
	protected UserBoundary user;
	protected ElementBoundary element;
	
	public Action(UserService userService,ExtraElementsService elementService,ActionBoundary action) {
		this.userService = userService;
		this.elementService = elementService;
		this.action = action;
	}
	
	
	public void init() {
		user = getActionUserBoundary();
		if(!isUserAPlayer(user))
		{
			throw new RuntimeException("the user is not a player");
		}
		element = getElementInAction();
		if(!element.isActive()) {
			throw new RuntimeException("the element is not active");
		}
	}
	
	private UserBoundary getActionUserBoundary() {
		return this.userService.login(action.getInvokedBy().getUserId().getDomain(), 
				action.getInvokedBy().getUserId().getEmail());
	}
	
	
	private ElementBoundary getElementInAction() {
		return this.elementService.getSpecificElement(
				action.getInvokedBy().getUserId().getDomain(),
				action.getInvokedBy().getUserId().getEmail(), 
				action.getElement().getElementId().getDomain(), 
				action.getElement().getElementId().getId());
	}
	
	
	private boolean isUserAPlayer(UserBoundary action) {
		if (user.getRole()!=UserRoles.PLAYER.toString()) {
			return false;
		}else {
			return true;
		}
	}
	
	abstract void invoke();
}
