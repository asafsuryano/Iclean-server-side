
package acs.serviceImplementation.actionServicePackage;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import acs.Utils.StringUtil;
import acs.actionBoundaryPackage.ActionBoundary;
import acs.dal.ActionDao;
import acs.data.ActionEntity;
import acs.data.ActionEntityBoundaryConverter;
import acs.data.UserRoles;
import acs.elementBoundaryPackage.ElementBoundary;
import acs.logic.ElementService;
import acs.logic.ExtraActionService;
import acs.logic.UserService;
import acs.usersBoundaryPackage.UserBoundary;


@Service
public class ActionServiceImplementation implements ExtraActionService {
	private ActionDao  actionsDatabase;
	private UserService userService;
	private ElementService elementService;
	private ActionEntityBoundaryConverter converter; 
	private ActionEntity invoke;
	private String projectName;
	private ActionManager actionMng;

	@Autowired
	public ActionServiceImplementation(ActionEntityBoundaryConverter converter,ActionDao actionsDatabase,
			ElementService elementService,UserService userService,ActionManager actionMng) {
		this.converter = converter;
		this.actionsDatabase=actionsDatabase;
		this.elementService=elementService;
		this.userService = userService;
		this.actionMng=actionMng;
	}
	

	@PostConstruct
	public void init() {
		//this.actionsDatabase = Collections.synchronizedMap(new HashMap<>());
	}

	// injection of value from the spring boot configuration
	@Value("${spring.application.name:demo}")
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	public Object invokeAction(ActionBoundary action) {
		if (StringUtil.isNullOrEmpty(action.getType()))
			throw new RuntimeException("empty type");
		if (StringUtil.isNullOrEmpty(action.getElement().getElementId().getDomain())
				||(StringUtil.isNullOrEmpty(action.getElement().getElementId().getId())))
				throw new RuntimeException("invalid elementId");
		if (StringUtil.isNullOrEmpty(action.getInvokedBy().getUserId().getDomain())
				|| (StringUtil.isNullOrEmpty(action.getInvokedBy().getUserId().getEmail())))
			throw new RuntimeException("invalid user details");
		actionMng.setAction(action);
		actionMng.getAction().invoke();
		action.setActionId(new acs.actionBoundaryPackage.ActionId(projectName, UUID.randomUUID().toString()));
		action.setCreatedTimestamp(new Date());
		this.invoke = this.converter.boundaryToEntity(action);
		this.actionsDatabase.save(this.invoke);
		return action;
	}


	@Override
	@Transactional(readOnly = true)
	public List<ActionBoundary> getAllActions(String adminDomain, String adminEmail) {
		return StreamSupport.stream(this.actionsDatabase.findAll().spliterator(),false).
				map(this.converter::entityToBoundary).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void deleteAllActions(String adminDomain, String adminEmail) {
		this.actionsDatabase.deleteAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActionBoundary> getAllActionsWithPagination(String adminDomain, 
			String adminEmail, int size, int page) {
		
		UserBoundary user= this.userService.login(adminDomain, adminEmail);
		if (user.getRole()!=UserRoles.ADMIN.toString()) {
			throw new RuntimeException("the user is not admin");
		}
		return this.actionsDatabase.findAll(PageRequest.of(page, size, Direction.DESC, "type")).getContent()
				.stream().map(this.converter::entityToBoundary).collect(Collectors.toList());
		
				
//				this.actionsDatabase.findAllById(PageRequest.of
//				(page, size))
//				.stream()
//				.map(this.converter::entityToBoundary)
//				.collect(Collectors.toList());
	}
	
	public boolean isElementInActionActive(ActionBoundary action) {
		ElementBoundary element=this.elementService.getSpecificElement(
				action.getInvokedBy().getUserId().getDomain(),
				action.getInvokedBy().getUserId().getEmail(), 
				action.getElement().getElementId().getDomain(), 
				action.getElement().getElementId().getId());
		return element.isActive();

	}
	public boolean isUserInActionAPlayer(ActionBoundary action) {
		UserBoundary user=this.userService.login(action.getInvokedBy().getUserId().getDomain(), 
				action.getInvokedBy().getUserId().getEmail());
		if (user.getRole()!=UserRoles.PLAYER.toString()) {
			return false;
		}else {
			return true;
		}
	}

}
