
package acs.serviceImplementation.actionServicePackage;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import acs.Utils.StringUtil;
import acs.actionBoundaryPackage.ActionBoundary;
import acs.data.ActionEntity;
import acs.data.ActionEntityBoundaryConverter;
import acs.data.dal.ActionDao;
import acs.logic.ActionService;


@Service
public class ActionServiceImplementation implements ActionService {
	private ActionDao  actionsDatabase;
	
	private ActionEntityBoundaryConverter converter; 
	private ActionEntity invoke;
	private String projectName;

	@Autowired
	public ActionServiceImplementation(ActionEntityBoundaryConverter converter,ActionDao actionsDatabase) {
		this.converter = converter;
		this.actionsDatabase=actionsDatabase;
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

}
