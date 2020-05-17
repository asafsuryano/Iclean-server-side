package acs.dal;

import org.springframework.data.repository.PagingAndSortingRepository;

import acs.data.ActionEntity;
import acs.data.actionEntityProperties.ActionId;

public interface ActionDao extends PagingAndSortingRepository<ActionEntity,ActionId> {}