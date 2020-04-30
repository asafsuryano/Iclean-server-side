package acs.dal;

import org.springframework.data.repository.CrudRepository;

import acs.data.ElementEntity;
import acs.data.elementEntityProperties.ElementId;

public interface ElementDao extends CrudRepository<ElementEntity,ElementId> {

}
