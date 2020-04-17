package acs.serviceImplementation.actionServicePackage;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acs.actionBoundaryPackage.ActionBoundary;
import acs.data.ActionEntity;
import acs.data.ActionEntityBoundaryConverter;
import acs.logic.ActionService;

public class ActionServiceImplementation implements ActionService {
	private Map<String, ActionEntity> actionsDatabase;
	private ActionEntityBoundaryConverter converter; 
	private ActionEntity invoke;
	
	@Autowired
	public ActionServiceImplementation(ActionEntityBoundaryConverter converter) {
		this.converter = converter;
	}
	
	@PostConstruct
	public void init() {
		this.actionsDatabase = Collections.synchronizedMap(new TreeMap<>());
	}
	
	
	@Override
	public Object invokeAction(ActionBoundary action) {
		action.getActionId().setId(UUID.randomUUID().toString());
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
