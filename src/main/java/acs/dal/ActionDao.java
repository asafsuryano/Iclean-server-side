package acs.dal;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import acs.data.ActionEntity;
import acs.data.ElementEntity;
import acs.data.actionEntityProperties.ActionId;

public interface ActionDao extends PagingAndSortingRepository<ActionEntity,ActionId> {
	
	
	
}