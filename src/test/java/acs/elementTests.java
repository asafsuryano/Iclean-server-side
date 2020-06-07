package acs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import acs.elementBoundaryPackage.ElementBoundary;
import acs.elementBoundaryPackage.ElementIdBoundary;
import acs.elementBoundaryPackage.Location;
import acs.newUserDetailsBoundaryPackage.NewUserDetails;
import acs.usersBoundaryPackage.UserBoundary;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class elementTests {
	private RestTemplate restTemplate;
	private String elementUrl;
	private String adminUrl;
	private String userUrl;
	private String paginationUrl;
	private int port;
	private UserBoundary admin;
	private UserBoundary manager;
	private UserBoundary player;

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		this.elementUrl = "http://localhost:" + this.port + "/acs/elements";
		this.adminUrl = "http://localhost:" + this.port + "/acs/admin";
		this.userUrl = "http://localhost:" + this.port + "/acs/users";
		this.paginationUrl = "?page={page}&size={size}";

	}

	@BeforeEach
	public void setup() {
		this.admin = this.restTemplate.postForObject(this.userUrl,
				new NewUserDetails("admini@na.com", "dana", ":)", "ADMIN"), UserBoundary.class);
		this.manager = this.restTemplate.postForObject(this.userUrl,
				new NewUserDetails("mana@new.com", "s1am", "((:))", "MANAGER"), UserBoundary.class);
		this.player = this.restTemplate.postForObject(this.userUrl,
				new NewUserDetails("playyy@new.com", "sa2m", "((:P))", "PLAYER"), UserBoundary.class);

	}

	@AfterEach
	public void tearDown() {
		// post admin
		UserBoundary admin1 = this.restTemplate.postForObject(this.userUrl,
				new NewUserDetails("ba@na.com", "dana", ":)", "ADMIN"), UserBoundary.class);

		// then delete all users/elements after every test
		this.restTemplate.delete(this.adminUrl + "/users/{adminDomain}/{adminEmail}", admin.getUserId().getDomain(),
				admin.getUserId().getEmail());

		this.restTemplate.delete(this.adminUrl + "/elements/{adminDomain}/{adminEmail}", admin.getUserId().getDomain(),
				admin.getUserId().getEmail());

	}

	// TEST1 - manager can create element

	@Test
	public void testCreateElementBoundaryByUserManagerAndCheckIfExistInTheDatabase() throws Exception {
		ElementBoundary elementBoundary = new ElementBoundary();
		elementBoundary.setName("ban");
		elementBoundary.setActive(true);
		elementBoundary.setType("DEMO_ELEMENT_location");

		ElementBoundary boundaryOnServer = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundary, ElementBoundary.class,
				manager.getUserId().getDomain(), manager.getUserId().getEmail());

		this.restTemplate.getForObject(this.elementUrl + "/{userDomain}/{userEmail}/{elementDomain}/{elementID}",
				ElementBoundary.class, manager.getUserId().getDomain(), manager.getUserId().getEmail(),
				boundaryOnServer.getElementId().getDomain(), boundaryOnServer.getElementId().getId());
	}

	// TEST2 - player can not create element

	@Test
	public void testCreateElementBoundaryByUserPlayerAndCheckIfExistInTheDatabase() throws Exception {

		ElementBoundary elementBoundary = new ElementBoundary();
		elementBoundary.setName("ban");
		elementBoundary.setActive(true);
		elementBoundary.setType("DEMO_ELEMENT_location");
		try {
			ElementBoundary boundaryOnServer = this.restTemplate.postForObject(
					this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundary, ElementBoundary.class,
					player.getUserId().getDomain(), player.getUserId().getEmail());
			fail();
		} catch (HttpServerErrorException ex) {
			assertTrue(ex.getMessage().contains("Only manager can create element"));
		}
	}

	// TEST3 - admin can not create element

	@Test
	public void testCreateElementBoundaryByUserAdminAndCheckIfExistInTheDatabase() throws Exception {

		ElementBoundary elementBoundary = new ElementBoundary();
		elementBoundary.setName("ban");
		elementBoundary.setActive(true);
		elementBoundary.setType("DEMO_ELEMENT_location");
		try {
			ElementBoundary boundaryOnServer = this.restTemplate.postForObject(
					this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundary, ElementBoundary.class,
					admin.getUserId().getDomain(), admin.getUserId().getEmail());
			fail();
		} catch (HttpServerErrorException ex) {
			assertTrue(ex.getMessage().contains("Only manager can create element"));
		}
	}

	// TEST4 - update element name (manager user)

	@Test
	public void testPutOfElementAndUpdateItsNameThenTheNameIsUpdatedInTheDataBaseByManager() throws Exception {

		ElementBoundary elementBoundary = new ElementBoundary();
		elementBoundary.setName("ban");
		elementBoundary.setActive(true);
		elementBoundary.setType("DEMO_ELEMENT_location");

		ElementBoundary boundaryOnServer = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundary, ElementBoundary.class,
				manager.getUserId().getDomain(), manager.getUserId().getEmail());
		String userDomain = boundaryOnServer.getCreatedBy().getUserId().getDomain();
		String userEmail = boundaryOnServer.getCreatedBy().getUserId().getEmail();
		String elementDomain = boundaryOnServer.getElementId().getDomain();
		String elementId = boundaryOnServer.getElementId().getId();

		ElementBoundary update = new ElementBoundary();
		update.setActive(true);
		update.setName("workedd");

		this.restTemplate.put(this.elementUrl + "/{userDomain}/{userEmail}/{elementDomain}/{elementID}", update,
				userDomain, userEmail, elementDomain, elementId);

		ElementBoundary updatedNameInElementBoundary = this.restTemplate.getForObject(
				this.elementUrl + "/{userDomain}/{userEmail}/{elementDomain}/{elementID}", ElementBoundary.class,
				userDomain, userEmail, elementDomain, elementId);

		assertThat(updatedNameInElementBoundary.getName()).isNotNull().isEqualTo(update.getName());
		assertThat(updatedNameInElementBoundary.getName()).isNotNull().isNotEqualTo(elementBoundary.getName());
	}

	// TEST5 - can not update element name (player user)

	@Test
	public void testPutOfElementAndUpdateItsNameThenTheNameIsUpdatedInTheDataBaseByPlayer() throws Exception {

		ElementBoundary elementBoundary = new ElementBoundary();
		elementBoundary.setName("ban");
		elementBoundary.setActive(true);
		elementBoundary.setType("DEMO_ELEMENT_location");

		ElementBoundary boundaryOnServer = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundary, ElementBoundary.class,
				manager.getUserId().getDomain(), manager.getUserId().getEmail());
		String userDomain = player.getUserId().getDomain();
		String userEmail = player.getUserId().getEmail();
		String elementDomain = boundaryOnServer.getElementId().getDomain();
		String elementId = boundaryOnServer.getElementId().getId();

		ElementBoundary update = new ElementBoundary();
		update.setActive(true);
		update.setName("workedd");
		try {
			this.restTemplate.put(this.elementUrl + "/{userDomain}/{userEmail}/{elementDomain}/{elementID}", update,
					userDomain, userEmail, elementDomain, elementId);

			fail();
		} // THEN we got an exception
		catch (HttpServerErrorException ex) {
			assertTrue(ex.getMessage().contains("Only manager can update  element"));
		}

	}

	// TEST6 - can not update element name (admin user)

	@Test
	public void testPutOfElementAndUpdateItsNameThenTheNameIsUpdatedInTheDataBaseByAdmin() throws Exception {

		ElementBoundary elementBoundary = new ElementBoundary();
		elementBoundary.setName("ban");
		elementBoundary.setActive(true);
		elementBoundary.setType("DEMO_ELEMENT_location");

		ElementBoundary boundaryOnServer = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundary, ElementBoundary.class,
				manager.getUserId().getDomain(), manager.getUserId().getEmail());
		String userDomain = admin.getUserId().getDomain();
		String userEmail = admin.getUserId().getEmail();
		String elementDomain = boundaryOnServer.getElementId().getDomain();
		String elementId = boundaryOnServer.getElementId().getId();

		ElementBoundary update = new ElementBoundary();
		update.setActive(true);
		update.setName("workedd");
		try {
			this.restTemplate.put(this.elementUrl + "/{userDomain}/{userEmail}/{elementDomain}/{elementID}", update,
					userDomain, userEmail, elementDomain, elementId);

			fail();
		}
		// THEN we got an exception
		catch (HttpServerErrorException ex) {
			assertTrue(ex.getMessage().contains("Only manager can update  element"));
		}

	}

	// TEST7 - id after post and get is the same

	@Test
	public void testPostNewElementThenTheDatabaseHasAnElementWithTheSameElementIdAsPosted() {

		ElementBoundary elementBoundary = new ElementBoundary();
		elementBoundary.setName("ban");
		elementBoundary.setActive(true);
		elementBoundary.setType("DEMO_ELEMENT_location");

		ElementBoundary newElementBoundary = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundary, ElementBoundary.class,
				manager.getUserId().getDomain(), manager.getUserId().getEmail());

		ElementIdBoundary currentElementIdPosted = this.restTemplate
				.getForObject(this.elementUrl + "/{userDomain}/{userEmail}/{elementDomain}/{elementID}",
						ElementBoundary.class, newElementBoundary.getCreatedBy().getUserId().getDomain(),
						newElementBoundary.getCreatedBy().getUserId().getEmail(),
						newElementBoundary.getElementId().getDomain(), newElementBoundary.getElementId().getId())
				.getElementId();

		assertThat(currentElementIdPosted.getId()).isNotNull().isEqualTo(newElementBoundary.getElementId().getId());
	}

	@Test
	public void testUpdateInactiveElement() throws Exception {

		// GIVEN database with inactive element

		ElementBoundary elementBoundary = new ElementBoundary();
		elementBoundary.setName("ban");
		elementBoundary.setActive(false);
		elementBoundary.setType("DEMO_ELEMENT_location");

		ElementBoundary newElementBoundary = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundary, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

		String userDomain = newElementBoundary.getCreatedBy().getUserId().getDomain();
		String userEmail = newElementBoundary.getCreatedBy().getUserId().getEmail();
		String elementDomain = newElementBoundary.getElementId().getDomain();
		String elementId = newElementBoundary.getElementId().getId();

		// WHEN we put an updated element
		try {
			ElementBoundary update = new ElementBoundary();
			update.setCreatedBy(newElementBoundary.getCreatedBy());
			update.setName("workedd"); // updated name
			this.restTemplate.put(this.elementUrl + "/{userDomain}/{userEmail}/{elementDomain}/{elementID}", update,
					userDomain, userEmail, elementDomain, elementId);

			fail();// when we reach this line thats mean the test failed
		} // THEN we got an exception
		catch (HttpServerErrorException ex) {
			assertTrue(ex.getMessage().contains("Element Not Active"));
		}

	}
	// TEST 8 create 5 element and check if exsist

	@Test
	public void testPostFiveElementsAndCheckIfTheyExistInTheDataBase() throws Exception { //
		// GIVEN the server is up //AND the DB contains 5 elements

		UserBoundary managerBoundary = this.restTemplate.postForObject(this.userUrl,
				new NewUserDetails("naory@gmail.com", "niv", ":)", "MANAGER"), UserBoundary.class);
		int numOfElements=5;
		List<ElementBoundary> AllElementsInDataBase =

				IntStream.range(1, numOfElements+1).mapToObj(i -> "test" + i).map(
						userName -> new ElementBoundary(userName, null, "DEMO_ELEMENT_location", true, null, null, null, null))
						.map(boundary -> this.restTemplate.postForObject(
								this.elementUrl + "/{managerDomain}/{managerEmail}", boundary, ElementBoundary.class,
								managerBoundary.getUserId().getDomain(), managerBoundary.getUserId().getEmail()))
						.collect(Collectors.toList());

		// WHEN we get all elements

		ElementBoundary[] results = this.restTemplate.getForObject(this.elementUrl + "/{userDomain}/{userEmail}"+this.paginationUrl,
				ElementBoundary[].class, AllElementsInDataBase.get(0).getCreatedBy().getUserId().getDomain(),
				AllElementsInDataBase.get(0).getCreatedBy().getUserId().getEmail(),0,numOfElements);

		// THEN they exists in DB
		assertThat(results).hasSize(AllElementsInDataBase.size()).usingRecursiveFieldByFieldElementComparator()
				.containsExactlyInAnyOrderElementsOf(AllElementsInDataBase);

	}

	// Test 9 create 3 elements and delete with user(ADMIN)

	@Test
	public void testPostThreeElementsThenDeleteAllThemsWihtAdminUser() throws Exception { //
		// GIVEN the server is up //AND the DB contains 3 elements and an admin

		UserBoundary adminBoundary = this.restTemplate.postForObject(this.userUrl,
				new NewUserDetails("te@sst.com", "baan", ":)", "ADMIN"), UserBoundary.class);

		UserBoundary managerBoundary = this.restTemplate.postForObject(this.userUrl,
				new NewUserDetails("naory@gmail.com", "niv", ":)", "MANAGER"), UserBoundary.class);

		List<ElementBoundary> AllElementsInDataBase = IntStream.range(1, 4).mapToObj(i -> "test" + i)
				.map(userName -> new ElementBoundary(userName, null, "DEMO_ELEMENT_location", true, null, null, null, null))
				.map(boundary -> this.restTemplate.postForObject(this.elementUrl + "/{managerDomain}/{managerEmail}",
						boundary, ElementBoundary.class, managerBoundary.getUserId().getDomain(),
						managerBoundary.getUserId().getEmail()))
				.collect(Collectors.toList());

		// WHEN we delete all elements in DB
		this.restTemplate.delete(this.adminUrl + "/elements/{adminDomain}/{adminEmail}",
				adminBoundary.getUserId().getDomain(), adminBoundary.getUserId().getEmail());

		// THEN the DB is empty

		UserBoundary[] results = this.restTemplate.getForObject(this.elementUrl + "/{userDomain}/{userEmail}",
				UserBoundary[].class, AllElementsInDataBase.get(0).getCreatedBy().getUserId().getDomain(),
				AllElementsInDataBase.get(0).getCreatedBy().getUserId().getEmail());

		assertThat(results).isEmpty();

	}

	// Test 10 create an element with empty name and check if throw exception

	@Test
	public void testPostAnElementWithEmptyNameAndThenThrowAnException() throws Exception { // GIVEN server is up

		// WHEN i post an element with empty name
		ElementBoundary newElementPosted = new ElementBoundary();
		newElementPosted.setActive(true);
		newElementPosted.setType("DEMO_ELEMENT_location");
		newElementPosted.setName(""); // empty name
		try {
			this.restTemplate.postForObject(this.elementUrl + "/{managerDomain}/{managerEmail}", newElementPosted,
					ElementBoundary.class, "test", "te@st.com");
			fail();
		} catch (HttpServerErrorException ex) {
			assertTrue(ex instanceof HttpServerErrorException);
		}

	} 
	
	
	// Test 11 create an element with empty type and check if throw exception

	@Test
	public void testPostAnElementWithEmptyTypeAndThenThrowAnException() throws Exception { // GIVEN server is up

		UserBoundary managerBoundary = this.restTemplate.postForObject(this.userUrl,
				new NewUserDetails("naory@gmail.com", "niv", ":)", "MANAGER"), UserBoundary.class);

		// WHEN i post an element with empty type
		ElementBoundary newElementPosted = new ElementBoundary();
		newElementPosted.setActive(true);
		newElementPosted.setType(""); // empty type
		newElementPosted.setName("jhon");
		try {
			this.restTemplate.postForObject(this.elementUrl + "/{managerDomain}/{managerEmail}", newElementPosted,
					ElementBoundary.class, managerBoundary.getUserId().getDomain(),
					managerBoundary.getUserId().getEmail());
			fail();
		}

		catch (HttpServerErrorException ex) {
			assertTrue(ex instanceof HttpServerErrorException);
		}

	}

	// Test 12 put 3 element Children of 1 origin ant save them in database
	@Test
	public void PutChildElementAndCheckIfTheyExsistInDataBaseByManager() {
		UserBoundary managerBoundary = this.restTemplate.postForObject(this.userUrl,
				new NewUserDetails("naory@gmail.com", "niv", ":)", "MANAGER"), UserBoundary.class);

		ElementBoundary elementBoundaryParent = new ElementBoundary();
		elementBoundaryParent.setName("ban");
		elementBoundaryParent.setActive(true);
		elementBoundaryParent.setType("DEMO_ELEMENT_location");

		ElementBoundary elementBoundaryChild1 = new ElementBoundary();
		elementBoundaryChild1.setName("ban");
		elementBoundaryChild1.setActive(true);
		elementBoundaryChild1.setType("DEMO_ELEMENT_location");

		ElementBoundary elementBoundaryChild2 = new ElementBoundary();
		elementBoundaryChild2.setName("ban");
		elementBoundaryChild2.setActive(true);
		elementBoundaryChild2.setType("DEMO_ELEMENT_location");

		ElementBoundary elementBoundaryChild3 = new ElementBoundary();
		elementBoundaryChild3.setName("ban");
		elementBoundaryChild3.setActive(true);
		elementBoundaryChild3.setType("DEMO_ELEMENT_location");

		ElementBoundary boundaryOnServerParent = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundaryParent, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

		ElementBoundary boundaryOnServerChild1 = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundaryChild1, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

		ElementBoundary boundaryOnServerChild2 = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundaryChild2, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

		ElementBoundary boundaryOnServerChild3 = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundaryChild3, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

		/// call to bind!!
		this.restTemplate.put(this.elementUrl + "/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}/children",
				boundaryOnServerChild1.getElementId(), this.manager.getUserId().getDomain(),
				this.manager.getUserId().getEmail(), boundaryOnServerParent.getElementId().getDomain(),
				boundaryOnServerParent.getElementId().getId());

		this.restTemplate.put(this.elementUrl + "/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}/children",
				boundaryOnServerChild2.getElementId(), this.manager.getUserId().getDomain(),
				this.manager.getUserId().getEmail(), boundaryOnServerParent.getElementId().getDomain(),
				boundaryOnServerParent.getElementId().getId());

		this.restTemplate.put(this.elementUrl + "/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}/children",
				boundaryOnServerChild3.getElementId(), this.manager.getUserId().getDomain(),
				this.manager.getUserId().getEmail(), boundaryOnServerParent.getElementId().getDomain(),
				boundaryOnServerParent.getElementId().getId());

		ElementBoundary[] elementBoundary = this.restTemplate.getForObject(
				this.elementUrl
						+ "/{userDomain}/{userEmail}/{elementDomain}/{elementID}/children?page={page}&size={size}",
				ElementBoundary[].class, this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail(),
				boundaryOnServerParent.getElementId().getDomain(), boundaryOnServerParent.getElementId().getId(), 0, 1);

		assertThat(elementBoundary).hasSize(1);

	}

	// Test 13 get All parent Element of exiting child
	@Test
	public void putElementChildAndElementParentAndRetriveElementParentbyUserAndChild() {
		ElementBoundary elementBoundaryParent = new ElementBoundary();
		elementBoundaryParent.setName("ban");
		elementBoundaryParent.setActive(true);
		elementBoundaryParent.setType("DEMO_ELEMENT_location");

		ElementBoundary elementBoundaryChild = new ElementBoundary();
		elementBoundaryChild.setName("niv");
		elementBoundaryChild.setActive(true);
		elementBoundaryChild.setType("DEMO_ELEMENT_location");

		ElementBoundary boundaryOnServerParent = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundaryParent, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

		ElementBoundary boundaryOnServerChild1 = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundaryChild, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

		this.restTemplate.put(this.elementUrl + "/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}/children",
				boundaryOnServerChild1.getElementId(), this.manager.getUserId().getDomain(),
				this.manager.getUserId().getEmail(), boundaryOnServerParent.getElementId().getDomain(),
				boundaryOnServerParent.getElementId().getId());

		ElementBoundary[] elements = this.restTemplate.getForObject(
				this.elementUrl + "/{userDomain}/{userEmail}/{elementDomain}/{elementID}/parents",
				ElementBoundary[].class, this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail(),
				boundaryOnServerChild1.getElementId().getDomain(), boundaryOnServerChild1.getElementId().getId(), 0, 1);

		assertThat(elements).hasSize(1);
		assertThat(elements[0].getElementId().getId()).isEqualTo(boundaryOnServerParent.getElementId().getId());
	}

	// Test 14 put 3 elements (1 of them is active=false) Children of 1 origin ant
	// save them in database and player tries to
	@Test
	public void PutChildElementAndCheckIfTheyExsistInDataBase() {

		// add 3 elements where 1 of them is active=false
		ElementBoundary elementBoundaryParent = new ElementBoundary();
		elementBoundaryParent.setName("ban");
		elementBoundaryParent.setActive(true);
		elementBoundaryParent.setType("DEMO_ELEMENT_location");

		ElementBoundary elementBoundaryChild1 = new ElementBoundary();
		elementBoundaryChild1.setName("ban");
		elementBoundaryChild1.setActive(true);
		elementBoundaryChild1.setType("DEMO_ELEMENT_location");

		ElementBoundary elementBoundaryChild2 = new ElementBoundary();
		elementBoundaryChild2.setName("ban");
		elementBoundaryChild2.setActive(true);
		elementBoundaryChild2.setType("DEMO_ELEMENT_location");

		ElementBoundary elementBoundaryChild3 = new ElementBoundary();
		elementBoundaryChild3.setName("ban");
		elementBoundaryChild3.setActive(false);
		elementBoundaryChild3.setType("DEMO_ELEMENT_location");

		ElementBoundary boundaryOnServerParent = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundaryParent, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

		ElementBoundary boundaryOnServerChild1 = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundaryChild1, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

		ElementBoundary boundaryOnServerChild2 = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundaryChild2, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

		ElementBoundary boundaryOnServerChild3 = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundaryChild3, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

		/// call to bind!!
		this.restTemplate.put(this.elementUrl + "/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}/children",
				boundaryOnServerChild1.getElementId(), this.manager.getUserId().getDomain(),
				this.manager.getUserId().getEmail(), boundaryOnServerParent.getElementId().getDomain(),
				boundaryOnServerParent.getElementId().getId());

		this.restTemplate.put(this.elementUrl + "/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}/children",
				boundaryOnServerChild2.getElementId(), this.manager.getUserId().getDomain(),
				this.manager.getUserId().getEmail(), boundaryOnServerParent.getElementId().getDomain(),
				boundaryOnServerParent.getElementId().getId());

		this.restTemplate.put(this.elementUrl + "/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}/children",
				boundaryOnServerChild3.getElementId(), this.manager.getUserId().getDomain(),
				this.manager.getUserId().getEmail(), boundaryOnServerParent.getElementId().getDomain(),
				boundaryOnServerParent.getElementId().getId());

		// Then only 2 children will be returned to user player
		ElementBoundary[] elementBoundary = this.restTemplate.getForObject(
				this.elementUrl + "/{userDomain}/{userEmail}/{elementDomain}/{elementID}/children" + this.paginationUrl,
				ElementBoundary[].class, this.player.getUserId().getDomain(), this.player.getUserId().getEmail(),
				boundaryOnServerParent.getElementId().getDomain(), boundaryOnServerParent.getElementId().getId(), 0, 3);

		assertThat(elementBoundary).hasSize(2);

	}

	// Test 15 add 3 elements to the database and find by name with size < 1
	@Test
	public void testAdd3ElementsToDatabaseAndCallSearchByNameWithSizeSmallerThen1() {
		// add 3 normal elements
		ElementBoundary elementBoundary1 = new ElementBoundary();
		elementBoundary1.setName("ban");
		elementBoundary1.setActive(true);
		elementBoundary1.setType("DEMO_ELEMENT_location");

		ElementBoundary elementBoundary2 = new ElementBoundary();
		elementBoundary2.setName("ban");
		elementBoundary2.setActive(true);
		elementBoundary2.setType("DEMO_ELEMENT_location");

		ElementBoundary elementBoundary3 = new ElementBoundary();
		elementBoundary3.setName("ban");
		elementBoundary3.setActive(true);
		elementBoundary3.setType("DEMO_ELEMENT_location");

		// post to database
		ElementBoundary boundaryOnServer1 = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundary1, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

		ElementBoundary boundaryOnServer2 = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundary2, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

		ElementBoundary boundaryOnServer3 = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundary3, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

		try {
			ElementBoundary[] elements = this.restTemplate.getForObject(
					this.elementUrl + "/{userDomain}/{userEmail}/search/byName/{name}" + this.paginationUrl,
					ElementBoundary[].class, this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail(),
					"ban", 0, 0);
		} catch (HttpServerErrorException ex) {
			assertTrue(ex.toString().contains("size cannot be less then 1"));
		}
	}

	// Test 16 add 3 elements to the database and find by name with page < 1
	@Test
	public void testAdd3ElementsToDatabaseAndCallSearchByNameWithPageSmallerThen0() {
		// add 3 normal elements
		ElementBoundary elementBoundary1 = new ElementBoundary();
		elementBoundary1.setName("ban");
		elementBoundary1.setActive(true);
		elementBoundary1.setType("DEMO_ELEMENT_location");

		ElementBoundary elementBoundary2 = new ElementBoundary();
		elementBoundary2.setName("ban");
		elementBoundary2.setActive(true);
		elementBoundary2.setType("DEMO_ELEMENT_location");

		ElementBoundary elementBoundary3 = new ElementBoundary();
		elementBoundary3.setName("ban");
		elementBoundary3.setActive(true);
		elementBoundary3.setType("DEMO_ELEMENT_location");

		// post to database
		ElementBoundary boundaryOnServer1 = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundary1, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

		ElementBoundary boundaryOnServer2 = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundary2, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

		ElementBoundary boundaryOnServer3 = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundary3, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

		try {
			ElementBoundary[] elements = this.restTemplate.getForObject(
					this.elementUrl + "/{userDomain}/{userEmail}/search/byName/{name}" + this.paginationUrl,
					ElementBoundary[].class, this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail(),
					"ban", -1, 1);
		} catch (HttpServerErrorException ex) {
			assertTrue(ex.toString().contains("page cannot be negative"));
		}
	}

	// Test 17 add 3 elements to the database and find by name with admin user
	@Test
	public void testAdd3ElementsToDatabaseAndCallSearchByNameWithAdminUser() {
		// add 3 normal elements
		ElementBoundary elementBoundary1 = new ElementBoundary();
		elementBoundary1.setName("ban");
		elementBoundary1.setActive(true);
		elementBoundary1.setType("DEMO_ELEMENT_location");

		ElementBoundary elementBoundary2 = new ElementBoundary();
		elementBoundary2.setName("ban");
		elementBoundary2.setActive(true);
		elementBoundary2.setType("DEMO_ELEMENT_location");

		ElementBoundary elementBoundary3 = new ElementBoundary();
		elementBoundary3.setName("ban");
		elementBoundary3.setActive(true);
		elementBoundary3.setType("DEMO_ELEMENT_location");

		// post to database
		ElementBoundary boundaryOnServer1 = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundary1, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

		ElementBoundary boundaryOnServer2 = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundary2, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

		ElementBoundary boundaryOnServer3 = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundary3, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

		try {
			ElementBoundary[] elements = this.restTemplate.getForObject(
					this.elementUrl + "/{userDomain}/{userEmail}/search/byName/{name}" + this.paginationUrl,
					ElementBoundary[].class, this.admin.getUserId().getDomain(), this.admin.getUserId().getEmail(),
					"ban", 0, 1);
		} catch (HttpServerErrorException ex) {
			assertTrue(ex.toString().contains("User Admin does not have permission"));
		}
	}

	// Test 18 add 3 elements to the database and find by name with manager user

	@Test
	public void testAdd3ElementsToDatabaseAndSearchThemByNameWithManagerUserOnlyReturns2Elements() {
		// add 3 normal elements
		ElementBoundary elementBoundary1 = new ElementBoundary();
		elementBoundary1.setName("ban");
		elementBoundary1.setActive(true);
		elementBoundary1.setType("DEMO_ELEMENT_location");

		ElementBoundary elementBoundary2 = new ElementBoundary();
		elementBoundary2.setName("ban");
		elementBoundary2.setActive(true);
		elementBoundary2.setType("DEMO_ELEMENT_location");

		ElementBoundary elementBoundary3 = new ElementBoundary();
		elementBoundary3.setName("hello");
		elementBoundary3.setActive(true);
		elementBoundary3.setType("DEMO_ELEMENT_location");

		// post to database
		ElementBoundary boundaryOnServer1 = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundary1, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

		ElementBoundary boundaryOnServer2 = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundary2, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

		ElementBoundary boundaryOnServer3 = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundary3, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

		ElementBoundary[] elements = this.restTemplate.getForObject(
				this.elementUrl + "/{userDomain}/{userEmail}/search/byName/{name}" + this.paginationUrl,
				ElementBoundary[].class, this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail(),
				"ban", 0, 3);

		assertThat(elements).hasSize(2);
	}

	// Test 19 add 3 elements(1 of them is active false) to the database and find by
	// name with player user

	@Test
	public void testAdd3ElementsWhen1OfThemIsActiveFalseToDatabaseAndSearchThemByNameOnlyReturns2Elements() {
		// add 3 normal elements
		ElementBoundary elementBoundary1 = new ElementBoundary();
		elementBoundary1.setName("ban");
		elementBoundary1.setActive(true);
		elementBoundary1.setType("DEMO_ELEMENT_location");

		ElementBoundary elementBoundary2 = new ElementBoundary();
		elementBoundary2.setName("ban");
		elementBoundary2.setActive(true);
		elementBoundary2.setType("DEMO_ELEMENT_location");

		ElementBoundary elementBoundary3 = new ElementBoundary();
		elementBoundary3.setName("ban");
		elementBoundary3.setActive(false);
		elementBoundary3.setType("DEMO_ELEMENT_location");

		// post to database
		ElementBoundary boundaryOnServer1 = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundary1, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

		ElementBoundary boundaryOnServer2 = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundary2, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

		ElementBoundary boundaryOnServer3 = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundary3, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

		ElementBoundary[] elements = this.restTemplate.getForObject(
				this.elementUrl + "/{userDomain}/{userEmail}/search/byName/{name}" + this.paginationUrl,
				ElementBoundary[].class, this.player.getUserId().getDomain(), this.player.getUserId().getEmail(), "ban",
				0, 3);

		assertThat(elements).hasSize(2);
	}

	// Test 20 add 3 elements(1 of them is active false) to the database and find by
	// name with manager user

	@Test
	public void testAdd3ElementsToDatabaseAndSearchThemByTypeOnlyReturns2Elements() {
		// add 3 normal elements
		ElementBoundary elementBoundary1 = new ElementBoundary();
		elementBoundary1.setName("ban");
		elementBoundary1.setActive(true);
		elementBoundary1.setType("DEMO_ELEMENT_location");

		ElementBoundary elementBoundary2 = new ElementBoundary();
		elementBoundary2.setName("ban");
		elementBoundary2.setActive(true);
		elementBoundary2.setType("DEMO_ELEMENT_location");

		ElementBoundary elementBoundary3 = new ElementBoundary();
		elementBoundary3.setName("ban");
		elementBoundary3.setActive(true);
		elementBoundary3.setType("DEMO");

		// post to database
		ElementBoundary boundaryOnServer1 = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundary1, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

		ElementBoundary boundaryOnServer2 = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundary2, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

		ElementBoundary boundaryOnServer3 = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundary3, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

		ElementBoundary[] elements = this.restTemplate.getForObject(
				this.elementUrl + "/{userDomain}/{userEmail}/search/byType/{type}" + this.paginationUrl,
				ElementBoundary[].class, this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail(),
				"DEMO_ELEMENT_location", 0, 3);

		assertThat(elements).hasSize(2);
	}

	// Test 21 add 3 elements(1 of them is active false) to the database and find by
	// type with player user

	@Test
	public void testAdd3ElementsToDatabaseWhen1IsActiveFalseAndSearchThemByTypeOnlyReturns2Elements() {
		// add 3 normal elements
		ElementBoundary elementBoundary1 = new ElementBoundary();
		elementBoundary1.setName("ban");
		elementBoundary1.setActive(true);
		elementBoundary1.setType("DEMO_ELEMENT_location");

		ElementBoundary elementBoundary2 = new ElementBoundary();
		elementBoundary2.setName("ban");
		elementBoundary2.setActive(true);
		elementBoundary2.setType("DEMO_ELEMENT_location");

		ElementBoundary elementBoundary3 = new ElementBoundary();
		elementBoundary3.setName("ban");
		elementBoundary3.setActive(false);
		elementBoundary3.setType("DEMO_ELEMENT_location");

		// post to database
		ElementBoundary boundaryOnServer1 = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundary1, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

		ElementBoundary boundaryOnServer2 = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundary2, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

		ElementBoundary boundaryOnServer3 = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundary3, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

		ElementBoundary[] elements = this.restTemplate.getForObject(
				this.elementUrl + "/{userDomain}/{userEmail}/search/byType/{type}" + this.paginationUrl,
				ElementBoundary[].class, this.player.getUserId().getDomain(), this.player.getUserId().getEmail(),
				"DEMO_ELEMENT_location", 0, 3);

		assertThat(elements).hasSize(2);
	}

	// Test 22 add 3 elements to the database and search nearest by some distance
	// with manager user

	@Test
	public void testAdd3ElementsToDatabaseAndSearchThemByLocationOnlyReturns2Elements() {
		// add 3 normal elements
		ElementBoundary elementBoundary1 = new ElementBoundary();
		elementBoundary1.setName("ban");
		elementBoundary1.setActive(true);
		elementBoundary1.setType("DEMO_ELEMENT_location");
		elementBoundary1.setLocation(new Location(5, 4));

		ElementBoundary elementBoundary2 = new ElementBoundary();
		elementBoundary2.setName("ban");
		elementBoundary2.setActive(true);
		elementBoundary2.setType("DEMO_ELEMENT_location");
		elementBoundary2.setLocation(new Location(12, 12));

		ElementBoundary elementBoundary3 = new ElementBoundary();
		elementBoundary3.setName("ban");
		elementBoundary3.setActive(false);
		elementBoundary3.setType("DEMO_ELEMENT_location");
		elementBoundary3.setLocation(new Location(15, 14));

		// post to database
		ElementBoundary boundaryOnServer1 = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundary1, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

		ElementBoundary boundaryOnServer2 = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundary2, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

		ElementBoundary boundaryOnServer3 = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundary3, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

		ElementBoundary[] elements = this.restTemplate.getForObject(
				this.elementUrl + "/{userDomain}/{userEmail}/search/near/{lat}/{lng}/{distance}" + this.paginationUrl,
				ElementBoundary[].class, this.player.getUserId().getDomain(), this.player.getUserId().getEmail(), 5, 5,
				10, 0, 3);

		assertThat(elements).hasSize(2);
	}
	
	
	// Test 23 add 3 location elements and 1 mission element to the database and search nearest by some distance
		// with manager user

		@Test
		public void testAdd3ElementsAndOneMissionElementToDatabaseAndSearchThemByLocation() {
			// add 3 normal elements
			ElementBoundary elementBoundary1 = new ElementBoundary();
			elementBoundary1.setName("ban");
			elementBoundary1.setActive(true);
			elementBoundary1.setType("DEMO_ELEMENT_location");
			elementBoundary1.setLocation(new Location(5, 4));

			ElementBoundary elementBoundary2 = new ElementBoundary();
			elementBoundary2.setName("ban");
			elementBoundary2.setActive(true);
			elementBoundary2.setType("DEMO_ELEMENT_location");
			elementBoundary2.setLocation(new Location(12, 12));

			ElementBoundary elementBoundary3 = new ElementBoundary();
			elementBoundary3.setName("ban");
			elementBoundary3.setActive(true);
			elementBoundary3.setType("DEMO_ELEMENT_location");
			elementBoundary3.setLocation(new Location(15, 14));
			
			ElementBoundary missionElementBoundary = new ElementBoundary();
			missionElementBoundary.setName("banShift");
			missionElementBoundary.setActive(true);
			missionElementBoundary.setType("shift");
			missionElementBoundary.setLocation(new Location(15, 14));

			// post to database
			ElementBoundary boundaryOnServer1 = this.restTemplate.postForObject(
					this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundary1, ElementBoundary.class,
					this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

			ElementBoundary boundaryOnServer2 = this.restTemplate.postForObject(
					this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundary2, ElementBoundary.class,
					this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

			ElementBoundary boundaryOnServer3 = this.restTemplate.postForObject(
					this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundary3, ElementBoundary.class,
					this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());
			
			ElementBoundary missionboundaryOnServer = this.restTemplate.postForObject(
					this.elementUrl + "/{managerDomain}/{managerEmail}", missionElementBoundary, ElementBoundary.class,
					this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());
			
			
			ElementBoundary[] elements = this.restTemplate.getForObject(
					this.elementUrl + "/{userDomain}/{userEmail}/search/near/{lat}/{lng}/{distance}" + this.paginationUrl,
					ElementBoundary[].class, this.player.getUserId().getDomain(), this.player.getUserId().getEmail(), 5, 5,
					100, 0, 3);

			assertThat(elements).hasSize(3);
			assertThat(elements).allMatch(x -> x.getType().equals("DEMO_ELEMENT_location"));
		}
		
		
		
		
		
		
		@Test
	    public void testCreateElementParentAndBindWith3elementChild() {
			//create Parent
			ElementBoundary elementBoundaryParent = new ElementBoundary();
			elementBoundaryParent.setName("YarkonPark");
			elementBoundaryParent.setActive(true);
			elementBoundaryParent.setType("DEMO_ELEMENT_location");
		
			
			//create 3 child 
			ElementBoundary elementBoundaryChild1 = new ElementBoundary();
			elementBoundaryChild1.setName("Area 2");
			elementBoundaryChild1.setActive(true);
			elementBoundaryChild1.setType("DEMO_ELEMENT_location");

			ElementBoundary elementBoundaryChild2 = new ElementBoundary();
			elementBoundaryChild2.setName("Area 2");
			elementBoundaryChild2.setActive(true);
			elementBoundaryChild2.setType("DEMO_ELEMENT_location");

			ElementBoundary elementBoundaryChild3 = new ElementBoundary();
			elementBoundaryChild3.setName("Area 3");
			elementBoundaryChild3.setActive(true);
			elementBoundaryChild3.setType("DEMO_ELEMENT_location");
			
			
			
			
			ElementBoundary boundaryOnServerParent = this.restTemplate.postForObject(
					this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundaryParent, ElementBoundary.class,
					this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

			ElementBoundary boundaryOnServerChild1 = this.restTemplate.postForObject(
					this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundaryChild1, ElementBoundary.class,
					this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

			ElementBoundary boundaryOnServerChild2 = this.restTemplate.postForObject(
					this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundaryChild2, ElementBoundary.class,
					this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

			ElementBoundary boundaryOnServerChild3 = this.restTemplate.postForObject(
					this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundaryChild3, ElementBoundary.class,
					this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());
			
			
			
			
			
			/// call to bind!!
			this.restTemplate.put(this.elementUrl + "/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}/children",
					boundaryOnServerChild1.getElementId(), this.manager.getUserId().getDomain(),
					this.manager.getUserId().getEmail(), boundaryOnServerParent.getElementId().getDomain(),
					boundaryOnServerParent.getElementId().getId());

			this.restTemplate.put(this.elementUrl + "/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}/children",
					boundaryOnServerChild2.getElementId(), this.manager.getUserId().getDomain(),
					this.manager.getUserId().getEmail(), boundaryOnServerParent.getElementId().getDomain(),
					boundaryOnServerParent.getElementId().getId());

			this.restTemplate.put(this.elementUrl + "/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}/children",
					boundaryOnServerChild3.getElementId(), this.manager.getUserId().getDomain(),
					this.manager.getUserId().getEmail(), boundaryOnServerParent.getElementId().getDomain(),
					boundaryOnServerParent.getElementId().getId());

			ElementBoundary[] elementBoundary = this.restTemplate.getForObject(
					this.elementUrl
							+ "/{userDomain}/{userEmail}/{elementDomain}/{elementID}/children?page={page}&size={size}",
					ElementBoundary[].class, this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail(),
					boundaryOnServerParent.getElementId().getDomain(), boundaryOnServerParent.getElementId().getId(), 0,3);

			assertThat(elementBoundary).hasSize(3);

		}
		 
		 //    (2: make bind to parent that the child is  Type shift ---- need to throw Execption!
	    // shift can be only child not parent !!
		
	@Test
	public void CreateParentAndBindHimWithShiftTypeAndCheckIfThrowException() {
		ElementBoundary elementBoundaryParent = new ElementBoundary();
		elementBoundaryParent.setName("YarkonPark");
		elementBoundaryParent.setActive(true);
		elementBoundaryParent.setType("DEMO_ELEMENT_location");
	
		
		
		ElementBoundary elementBoundaryChild1 = new ElementBoundary();
		elementBoundaryChild1.setName("Area 1");
		elementBoundaryChild1.setActive(true);
		elementBoundaryChild1.setType("Shift");
		
		
		
		
		
		ElementBoundary boundaryOnServerParent = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundaryParent, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

		
		
		
		ElementBoundary boundaryOnServerChild1 = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundaryChild1, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

		
		
		try {
			this.restTemplate.put(this.elementUrl + "/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}/children",
					boundaryOnServerChild1.getElementId(), this.manager.getUserId().getDomain(),
					this.manager.getUserId().getEmail(), boundaryOnServerParent.getElementId().getDomain(),
					boundaryOnServerParent.getElementId().getId());
			
			fail();
	
		}catch(HttpServerErrorException ex) {
			assertTrue(ex.getMessage().contains("Shift Type Cannot Have a Parent"));
		}
		
		
	}
	
	
	
	
	
	@Test 
	public void Create2ElementsAndReplaceTheirParent() {
		
		
		
		ElementBoundary elementBoundaryParent = new ElementBoundary();
		elementBoundaryParent.setName("YarkonPark");
		elementBoundaryParent.setActive(true);
		elementBoundaryParent.setType("Shift");
		
		

		ElementBoundary elementBoundaryParent2 = new ElementBoundary();
		elementBoundaryParent2.setName("Afake");
		elementBoundaryParent2.setActive(true);
		elementBoundaryParent2.setType("Shift");
		
		
	
		ElementBoundary elementBoundaryChild1 = new ElementBoundary();
		elementBoundaryChild1.setName("child 1");
		elementBoundaryChild1.setActive(true);
		elementBoundaryChild1.setType("DEMO_ELEMENT_location");

		ElementBoundary elementBoundaryChild2 = new ElementBoundary();
		elementBoundaryChild2.setName("child 2 ");
		elementBoundaryChild2.setActive(true);
		elementBoundaryChild2.setType("DEMO_ELEMENT_location");
		
		
		//create 2 parent element
		
		ElementBoundary boundaryOnServerParent = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundaryParent, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());
		
		
		
		ElementBoundary boundaryOnServerParent2 = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundaryParent2, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());
		

		
		//Create 2 child element
		ElementBoundary boundaryOnServerChild1 = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundaryChild1, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

		ElementBoundary boundaryOnServerChild2 = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundaryChild2, ElementBoundary.class,
				this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());
		
		
		
		
	                
		/// Call to bind with parent 1!!
		this.restTemplate.put(this.elementUrl + "/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}/children",
				boundaryOnServerChild1.getElementId(), this.manager.getUserId().getDomain(),
				this.manager.getUserId().getEmail(), boundaryOnServerParent.getElementId().getDomain(),
				boundaryOnServerParent.getElementId().getId());

				this.restTemplate.put(this.elementUrl + "/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}/children",
						boundaryOnServerChild2.getElementId(), this.manager.getUserId().getDomain(),
						this.manager.getUserId().getEmail(), boundaryOnServerParent.getElementId().getDomain(),
						boundaryOnServerParent.getElementId().getId());
		

				
				
				
			  	/// Call to bind for the same children with other Parent!!
				this.restTemplate.put(this.elementUrl + "/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}/children",
						boundaryOnServerChild1.getElementId(), this.manager.getUserId().getDomain(),
						this.manager.getUserId().getEmail(), boundaryOnServerParent2.getElementId().getDomain(),
						boundaryOnServerParent2.getElementId().getId());

				this.restTemplate.put(this.elementUrl + "/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}/children",
						boundaryOnServerChild2.getElementId(), this.manager.getUserId().getDomain(),
						this.manager.getUserId().getEmail(), boundaryOnServerParent2.getElementId().getDomain(),
						boundaryOnServerParent2.getElementId().getId());
				
				
				
		    
				ElementBoundary[] elements = this.restTemplate.getForObject(
						this.elementUrl + "/{userDomain}/{userEmail}/{elementDomain}/{elementID}/parents",
						ElementBoundary[].class, this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail(),
						boundaryOnServerChild1.getElementId().getDomain(), boundaryOnServerChild1.getElementId().getId(), 0, 1);
				
				
				            
				assertTrue(elements[0].getElementId().getId().contains(boundaryOnServerParent2.getElementId().getId()));
				 
	}
		
		
	
}
