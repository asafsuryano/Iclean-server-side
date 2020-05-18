package acs.dal;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import acs.data.ElementEntity;
import acs.data.elementEntityProperties.ElementId;
import acs.data.elementEntityProperties.Location;
public interface ElementDao extends PagingAndSortingRepository<ElementEntity,ElementId> {

	
	//select .... From elementEntity where active=?
	public List<ElementEntity> findAllByActive(@Param("active") boolean isActive,Pageable pageable);
	
	//	SELECT ... From ElementEntity where parentDomain=?and parentId=? for managers
	public  List<ElementEntity>  findAllByParent(@Param("parentDomain") String parentDomain,
			@Param("parentId") String parentId,
			Pageable pageable);
	
	
	//	SELECT ... From ElementEntity where parentDomain=?and parentId=? for players
	public  List<ElementEntity>  findAllByParentAndActive(@Param("parentDomain") String parentDomain,
			@Param("parentId") String parentId,@Param("active") boolean isActive,
			Pageable pageable);
	
	
	// select ... from ElementEntity where name=? this query is for managers
	public List<ElementEntity> findAllByName(@Param("name") String name,Pageable pageable);
	
	// select ... from ElementEntity where name=? this query is for player
	public List<ElementEntity> findAllByNameAndActive(@Param("name") String name,
			@Param("active") boolean isActive,Pageable pageable);

	
	// select ... from ElementEntity where type=? this query is for managers
	public List<ElementEntity> findAllByType(@Param("type") String name,Pageable pageable);
	
	
	// select ... from ElementEntity where type=? this query is for players
	public List<ElementEntity> findAllByTypeAndActive(@Param("type") String name
			,@Param("active") boolean isActive,Pageable pageable);


	// select ... from ElementEntity where lat between minLat and maxLat and lng between minlng and maxlng for managers
	public List<ElementEntity> findAllByLocationLatBetweenAndLocationLngBetween(@Param("lat1") double minLat,
			@Param("lat2") double maxLat,@Param("lng1") double minLng,
					@Param("lng2") double maxLng,Pageable pageable);
	
	// select ... from ElementEntity where lat between minLat and maxLat and lng between minlng and maxlng for players
	public List<ElementEntity> findAllByLocationLatBetweenAndLocationLngBetweenAndActive(@Param("lat1") double minLat,
			@Param("lat2") double maxLat,@Param("lng1") double minLng,
					@Param("lng2") double maxLng,@Param("active") boolean isActive,Pageable pageable);

}
