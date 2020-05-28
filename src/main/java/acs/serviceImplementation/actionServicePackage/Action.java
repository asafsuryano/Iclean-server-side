package acs.serviceImplementation.actionServicePackage;

import javax.annotation.PostConstruct;
import javax.el.ELManager;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import acs.actionBoundaryPackage.ActionBoundary;
import acs.data.UserRoles;
import acs.elementBoundaryPackage.ElementBoundary;
import acs.logic.ElementService;
import acs.logic.UserService;
import acs.usersBoundaryPackage.UserBoundary;

public abstract class Action {
	
	
	protected UserService userService;
	protected ElementService elementService;
	protected ActionBoundary action;
	protected UserBoundary user;
	protected ElementBoundary element;
	
	public Action(UserService userService,ElementService elementService,ActionBoundary action) {
		this.userService = userService;
		this.elementService = elementService;
		this.action = action;
	}
	
	
	@PostConstruct
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
	
	abstract void invoke() throws JsonMappingException, JsonProcessingException;
}
