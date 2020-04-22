package acs.serviceImplementation.actionServicePackage;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import acs.actionBoundaryPackage.ActionBoundary;
import acs.data.ActionEntity;
import acs.data.ActionEntityBoundaryConverter;
import acs.data.actionEntityProperties.ActionId;
import acs.logic.ActionService;


@Service
public class ActionServiceImplementation implements ActionService {
	private Map<ActionId, ActionEntity> actionsDatabase;
	private ActionEntityBoundaryConverter converter; 
	private ActionEntity invoke;
	private String projectName;

	@Autowired
	public ActionServiceImplementation(ActionEntityBoundaryConverter converter) {
		this.converter = converter;
	}

	@PostConstruct
	public void init() {
		this.actionsDatabase = Collections.synchronizedMap(new HashMap<>());
	}

	// injection of value from the spring boot configuration
	@Value("${spring.application.name:demo}")
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	public Object invokeAction(ActionBoundary action) {
		action.setActionId(new acs.actionBoundaryPackage.ActionId(projectName, UUID.randomUUID().toString()));
		action.setCreatedTimestamp(new Date());
		this.invoke = this.converter.boundaryToEntity(action);
		return action;

	}


	@Override
	public List<ActionBoundary> getAllActions(String adminDomain, String adminEmail) {
		return this.actionsDatabase // Map<String, DummyEntity>
				.values()           // Collection<DummyEntity>
				.stream()		    // Stream<DummyEntity>
				.map(this.converter::entityToBoundary)	// Stream<DummyBoundaries>		
				.collect(Collectors.toList());

	}

	@Override
	public void deleteAllActions(String adminDomain, String adminEmail) {
		this.actionsDatabase
		.clear();
	}

}
