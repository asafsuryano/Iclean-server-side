package acs.data.dal;

import org.springframework.data.repository.CrudRepository;

import acs.data.ActionEntity;
import acs.data.actionEntityProperties.ActionId;

public interface ActionDao extends CrudRepository<ActionEntity,ActionId> {}