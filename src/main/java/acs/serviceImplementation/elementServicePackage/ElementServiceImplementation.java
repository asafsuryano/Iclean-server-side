package acs.serviceImplementation.elementServicePackage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import acs.dal.ElementDao;
import acs.data.ElementEntity;
import acs.data.ElementEntityBoundaryConverter;
import acs.data.UserRoles;
import acs.data.elementEntityProperties.ElementId;
import acs.elementBoundaryPackage.CreatedBy;
import acs.elementBoundaryPackage.ElementBoundary;
import acs.elementBoundaryPackage.ElementIdBoundary;
import acs.elementBoundaryPackage.Location;
import acs.elementBoundaryPackage.UserId;
import acs.logic.ExtraElementsService;
import acs.logic.UserService;
import acs.usersBoundaryPackage.UserBoundary;

@Service
public class ElementServiceImplementation implements ExtraElementsService {
	private String projectName;
	private ElementEntityBoundaryConverter converter;
	private ElementDao elementDatabase;
	private UserService userService;
	
	
	@Autowired
	public ElementServiceImplementation(ElementDao elementDao, ElementEntityBoundaryConverter converter,
			UserService userService) {
		this.converter = converter;
		this.elementDatabase = elementDao;
		this.userService = userService;
	}

	// injection of value from the spring boot configuration
	@Value("${spring.application.name:demo}")
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	@Transactional
	public ElementBoundary create(String managerDomain, String managerEmail, ElementBoundary element) {
		UserBoundary user = this.userService.login(managerDomain, managerEmail);
		UserRoles role = UserRoles.valueOf(user.getRole());
		if (role == UserRoles.MANAGER) {

			ElementIdBoundary elementId = new ElementIdBoundary(this.projectName, UUID.randomUUID().toString());
			element.setElementId(elementId);

			if (element.getType() == null) {
				element.setType("");
			}
			if (element.isActive() == null) {
				element.setActive(true);
			}

			if (element.getName() == null) {
				element.setName("");
			}

			element.setCreatedTimestamp(new Date());
			element.setCreatedBy(new CreatedBy(new UserId(managerDomain, managerEmail)));

			if (element.getElementAttributes() == null) {
				element.setElementAttributes(new LinkedHashMap<>());
			}

			// need to get location GPS from the client and give him also option to enter
			// manually
			// in case he has no reception
			if (element.getLocation() == null) {
				element.setLocation(new Location(0, 0));
			}

			ElementEntity el = this.converter.boundaryToEntity(element);

			this.elementDatabase.save(el);
			return element;
		} else {
			throw new RuntimeException("Only manager can create element");
		}
	}

	@Override
	@Transactional
	public ElementBoundary update(String managerDomain, String managerEmail, String elementDomain, String elementId,
			ElementBoundary update) {
		UserBoundary user = this.userService.login(managerDomain, managerEmail);
		UserRoles role = UserRoles.valueOf(user.getRole());
		if (role == UserRoles.MANAGER) {

			acs.data.elementEntityProperties.ElementId id = new acs.data.elementEntityProperties.ElementId(
					elementDomain, elementId);
			Optional<ElementEntity> element = this.elementDatabase.findById(id);
			if (!element.isPresent()) {
				throw new RuntimeException("Invalid Element");
			}
			if (element.get().isActive() == false && (update.isActive() == null || update.isActive() == false)) {
				throw new RuntimeException("Element Not Active! Cannot update...");
			}

			boolean dirty = false;

			if (update.getType() != null) {
				element.get().setType(update.getType());
				dirty = true;
			}

			if (update.isActive() != null) {
				element.get().setActive(update.isActive());
				dirty = true;
			}

			if (update.getName() != null) {
				element.get().setName(update.getName());
				dirty = true;
			}

			if (update.getElementAttributes() != null) {
				element.get().setElementAttributes(update.getElementAttributes());
				dirty = true;
			}

			if (update.getLocation() != null) {
				element.get().setLocation(new acs.data.elementEntityProperties.Location(update.getLocation().getLat(),
						update.getLocation().getLng()));
				dirty = true;
			}
			if (dirty) {
				this.elementDatabase.save(element.get());
			}

			return this.converter.entityToBoundary(element.get());
		} else {
			throw new RuntimeException("Only manager can update  element");

		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementBoundary> getAll(String userDomain, String userEmail) {
		UserBoundary user = this.userService.login(userDomain, userEmail);
		UserRoles role = UserRoles.valueOf(user.getRole());
		if (role == UserRoles.ADMIN) {
			throw new RuntimeException("Admin cannot retrive all element");
		}
		List<ElementBoundary> allElements = StreamSupport.stream(this.elementDatabase.findAll().spliterator(), false)
				.map(this.converter::entityToBoundary).collect(Collectors.toList());
		if (role == UserRoles.PLAYER) {
			for (int i = 0; i < allElements.size(); i++) {
				if (allElements.get(i).isActive() == false) {
					allElements.remove(i);
					i = -1;
				}
			}
		}
		return allElements;
	}

	@Override
	@Transactional(readOnly = true)
	public ElementBoundary getSpecificElement(String userDomain, String userEmail, String elementDomain,
			String elementId) {

		UserBoundary user = this.userService.login(userDomain, userEmail);
		UserRoles role = UserRoles.valueOf(user.getRole());
		if (role == UserRoles.ADMIN) {
			throw new RuntimeException("Admin cannot retrive specific element");
		}

		acs.data.elementEntityProperties.ElementId id = new acs.data.elementEntityProperties.ElementId(elementDomain,
				elementId);
		Optional<ElementEntity> elEntity = this.elementDatabase.findById(id);
		if (elEntity.isPresent()) {
			if (role == UserRoles.MANAGER)
				return this.converter.entityToBoundary(elEntity.get());
			else {
				if (role == UserRoles.PLAYER && elEntity.get().isActive())
					return this.converter.entityToBoundary(elEntity.get());
				else
					throw new ElementIsNotActiveException("the element does not exist");
			}

		} else
			throw new RuntimeException("the element does not exist");
	}

	@Override
	@Transactional
	public void deleteAllElements(String adminDomain, String adminEmail) {
		this.elementDatabase.deleteAll();
	}

	@Override
	@Transactional
	public void bindParentToChildElements(ElementBoundary parentElement, ElementBoundary childElement) {
		// TODO Auto-generated method stub

		ElementEntity parentEntity = this.elementDatabase
				.findById(new ElementId(parentElement.getElementId().getDomain(), parentElement.getElementId().getId()))
				.orElseThrow(() -> new RuntimeException("the element does not exist"));
		ElementEntity childEntity = this.elementDatabase
				.findById(new ElementId(childElement.getElementId().getDomain(), childElement.getElementId().getId()))
				.orElseThrow(() -> new RuntimeException("the element does not exist"));
		childEntity.setParent(parentEntity);
		this.elementDatabase.save(childEntity);
	}

	@Override
	@Transactional(readOnly = true)
	public Set<ElementBoundary> getAllChildren(ElementBoundary originId) {
		ElementEntity entity = this.elementDatabase
				.findById(new ElementId(originId.getElementId().getDomain(), originId.getElementId().getId()))
				.orElseThrow(() -> new RuntimeException("the element does not exist"));
		return entity.getChildren().stream().map(this.converter::entityToBoundary).collect(Collectors.toSet());
	}

	@Override
	@Transactional(readOnly = true)
	// return all elements with paganation
	public ElementBoundary[] getAllElementsWithPagination(String userDomain, String userEmail, int size, int page) {
		UserBoundary user = this.userService.login(userDomain, userEmail);
		UserRoles role = UserRoles.valueOf(user.getRole());
		errorCheckingSizePageAndAdmin(size, page, role);
		List<ElementBoundary> allElements = new ArrayList<ElementBoundary>();
		if (role==UserRoles.MANAGER)
			allElements = this.elementDatabase.findAll(PageRequest.of(page, size, Direction.DESC, "elementId")).getContent()
					.stream().map(this.converter::entityToBoundary).collect(Collectors.toList());
		else
			allElements = this.elementDatabase.findAllByActive(true,PageRequest.of(page, size, Direction.DESC, "elementId"))
			.stream().map(this.converter::entityToBoundary).collect(Collectors.toList());
		return allElements.toArray(new ElementBoundary[0]);
	}

	@Override
	@Transactional(readOnly = true)
	// return children of parent element with paganation
	public ElementBoundary[] getChildrenElements(String userDomain, String userEmail, String parentDomain,
			String parentId, int size, int page) {
		UserBoundary user = this.userService.login(userDomain, userEmail);
		UserRoles role = UserRoles.valueOf(user.getRole());
		errorCheckingSizePageAndAdmin(size, page, role);
		List<ElementBoundary> allElements = new ArrayList<ElementBoundary>();
		allElements = this.elementDatabase
				.findAllByParent(parentDomain, parentId, PageRequest.of(page, size, Direction.DESC, "timestamp", "elementId"))
				.stream().map(converter::entityToBoundary).collect(Collectors.toList());
		return allElements.toArray(new ElementBoundary[0]);
	}

	@Override
	@Transactional(readOnly = true)
	public ElementBoundary[] getAllParentsOfElement(String userDomain, String userEmail, String childDomain,
			String childId, int size, int page) {
		UserBoundary user = this.userService.login(userDomain, userEmail);
		UserRoles role = UserRoles.valueOf(user.getRole());
		ElementEntity elementChild = this.elementDatabase.findById(new ElementId(childDomain, childId))
				.orElseThrow(() -> new RuntimeException("the element does not exist"));
		errorCheckingSizePageAndAdmin(size, page, role);
		ElementEntity parentElement = elementChild.getParent();
		if (role == UserRoles.PLAYER && parentElement.isActive() == false)
			throw new RuntimeException("the parent is not active");
		Collection<ElementBoundary> elementsBoundry = new HashSet<>();

		if (parentElement != null && page == 0) {
			ElementBoundary rvBoundary = this.converter.entityToBoundary(parentElement);
			elementsBoundry.add(rvBoundary);
		}
		return elementsBoundry.toArray(new ElementBoundary[0]);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementBoundary> getElementsWithSpecificNameWithPagination(String userDomain, String userEmail,
			String name, int size, int page) {
		UserBoundary user = this.userService.login(userDomain, userEmail);
		UserRoles role = UserRoles.valueOf(user.getRole());
		errorCheckingSizePageAndAdmin(size, page, role);
		List<ElementBoundary> elements =new ArrayList<>();
		if (role==UserRoles.MANAGER)
			elements = elementDatabase
				.findAllByName(name, PageRequest.of(page, size, Direction.DESC, "elementId")).stream()
				.map(this.converter::entityToBoundary).collect(Collectors.toList());
		else
			elements = elementDatabase
			.findAllByNameAndActive(name,true ,PageRequest.of(page, size, Direction.DESC, "elementId")).stream()
			.map(this.converter::entityToBoundary).collect(Collectors.toList());

		return elements;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementBoundary> getElementsWithSpecificTypeWithPagination(String userDomain, String userEmail,
			String type, int size, int page) {
		UserBoundary user = this.userService.login(userDomain, userEmail);
		UserRoles role = UserRoles.valueOf(user.getRole());
		List<ElementBoundary> elements=new ArrayList<>();
		errorCheckingSizePageAndAdmin(size, page, role);
		if (role == UserRoles.MANAGER)
		elements = elementDatabase
				.findAllByType(type, PageRequest.of(page, size, Direction.DESC, "elementId")).stream()
				.map(this.converter::entityToBoundary).collect(Collectors.toList());
		else
			elements = elementDatabase
			.findAllByTypeAndActive(type,true ,PageRequest.of(page, size, Direction.DESC, "elementId")).stream()
			.map(this.converter::entityToBoundary).collect(Collectors.toList());

		return elements;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementBoundary> getElementsNearWithPagination(String userDomain, String userEmail, double lat,
			double lng, double distance, int size, int page) {
		UserBoundary user = this.userService.login(userDomain, userEmail);
		UserRoles role = UserRoles.valueOf(user.getRole());
		errorCheckingSizePageAndAdmin(size, page, role);

		List<ElementBoundary> allElements = new ArrayList<>();
		if (role==UserRoles.MANAGER)
			allElements = this.elementDatabase
					.findAllByLocationLatBetweenAndLocationLngBetween(lat-distance,lat+distance, 
							lng-distance, lng+distance,
							PageRequest.of(page, size, Direction.DESC, "elementId"))
					.stream().map(this.converter::entityToBoundary).collect(Collectors.toList());
		else
			allElements = this.elementDatabase
			.findAllByLocationLatBetweenAndLocationLngBetweenAndActive(lat-distance,lat+distance, 
					lng-distance, lng+distance, true,
					PageRequest.of(page, size, Direction.DESC, "elementId"))
			.stream().map(this.converter::entityToBoundary).collect(Collectors.toList());

		return allElements;
	}
	
	
	public void errorCheckingSizePageAndAdmin(int size,int page,UserRoles role)
	{
		if (role == UserRoles.ADMIN) {
			throw new RuntimeException("User Admin does not have permission ");
		}
		if (size < 1)
			throw new RuntimeException("size cannot be less then 1");
		if (page < 0)
			throw new RuntimeException("page cannot be negative");
	}
}
