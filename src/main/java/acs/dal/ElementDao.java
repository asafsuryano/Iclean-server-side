package acs.dal;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import acs.data.ElementEntity;
import acs.data.elementEntityProperties.ElementId;
public interface ElementDao extends PagingAndSortingRepository<ElementEntity,ElementId> {

	
	//	SELECT ... From ElementEntity where parentDomain=?and parentId=?
	public  List<ElementEntity>  findAllByParent(@Param("parentDomain") String parentDomain,
			@Param("parentId") String parentId,
			Pageable pageable);
	
	// select ... from ElementEntity where name=?
	public List<ElementEntity> findAllByName(@Param("name") String name,Pageable pageable);
	
	// select ... from ElementEntity where type=?
	public List<ElementEntity> findAllByType(@Param("type") String name,Pageable pageable);
	
	// select ... from ElementEntity where lat between minLat and maxLat and lng between minlng and maxlng
	public List<ElementEntity> findAllByLatBetweenAndLngBetween(@Param("lat") double minLat,
			@Param("lat") double maxLat,@Param("lng") double minLng,@Param("lng") double maxLng,Pageable pageable);

}
