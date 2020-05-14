package acs.dal;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import acs.data.ElementEntity;
import acs.data.elementEntityProperties.ElementId;
public interface ElementDao extends PagingAndSortingRepository<ElementEntity,ElementId> {

	public  List<ElementEntity>  findAllByParent(@Param("parentDomain") String parentDomain,
			@Param("parentId") String parentId,
			Pageable pageable);

}
