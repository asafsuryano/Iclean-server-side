
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
import acs.dal.ElementDao;
import acs.dal.UserDao;
import acs.data.ActionEntity;
import acs.data.ActionEntityBoundaryConverter;
import acs.data.ElementEntity;
import acs.data.UserEntity;
import acs.data.UserRoles;
import acs.data.elementEntityProperties.ElementId;
import acs.data.userEntityProperties.User;
import acs.elementBoundaryPackage.ElementBoundary;
import acs.logic.ActionService;
import acs.logic.ExtraActionService;


@Service
public class ActionServiceImplementation implements ExtraActionService {
	private ActionDao  actionsDatabase;
	private UserDao userDatabase;
	private ElementDao elementDatabase;
	private ActionEntityBoundaryConverter converter; 
	private ActionEntity invoke;
	private String projectName;

	@Autowired
	public ActionServiceImplementation(ActionEntityBoundaryConverter converter,ActionDao actionsDatabase,UserDao userDatabase,
			ElementDao elementDatabase) {
		this.converter = converter;
		this.actionsDatabase=actionsDatabase;
		this.userDatabase=userDatabase;
		this.elementDatabase=elementDatabase;
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
		if (isUserInActionAPlayer(action)==false) {
			throw new RuntimeException("the user is not a player");
		}
		if (isElementInActionActive(action)==false) {
			throw new RuntimeException("the element is not active");
		}
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
	public List<ActionBoundary> getAllActionsWithPagination(String adminDomain, String adminEmail, int size, int page) {
		UserEntity user=this.userDatabase.findById(new User(adminDomain, adminEmail))
				.orElseThrow(()->new RuntimeException("user does not exist"));
		if (user.getRole()!=UserRoles.ADMIN) {
			throw new RuntimeException("the user is not admin");
		}
		return this.actionsDatabase.findAll(PageRequest.of
				(page, size, Direction.DESC, "id"))
				.stream()
				.map(this.converter::entityToBoundary)
				.collect(Collectors.toList());
	}
	
	public boolean isElementInActionActive(ActionBoundary action) {
		ElementEntity element=this.elementDatabase.findById(new ElementId(action.getElement().getElementId().getDomain(), action.getElement().getElementId().getId()))
				.orElseThrow(()->new RuntimeException("the element does not exist"));
		return element.isActive();

	}
	public boolean isUserInActionAPlayer(ActionBoundary action) {
		UserEntity user=this.userDatabase.findById(new User(action.getInvokedBy().getUserId().getDomain(), 
				action.getInvokedBy().getUserId().getEmail()))
				.orElseThrow(()->new RuntimeException("user does not exist"));
		if (user.getRole()!=UserRoles.PLAYER) {
			return false;
		}else {
			return true;
		}
	}

}
