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
import acs.newUserDetailsBoundaryPackage.NewUserDetails;
import acs.usersBoundaryPackage.UserBoundary;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class elementTests {
	private RestTemplate restTemplate;
	private String elementUrl;
	private String adminUrl;
	private String userUrl;
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
		// // post admin
		// UserBoundary admin1 =
		// this.restTemplate
		// .postForObject(this.userUrl,
		// new NewUserDetails("ba@na.com","dana",":)","ADMIN"),
		// UserBoundary.class);

		// then delete all users/elements after every test
		this.restTemplate.delete(this.adminUrl + "/users/{adminDomain}/{adminEmail}", admin.getUserId().getDomain(),
				admin.getUserId().getEmail());

		this.restTemplate.delete(this.adminUrl + "/elements/{adminDomain}/{adminEmail}", admin.getUserId().getDomain(),
				admin.getUserId().getEmail());

	}

	/*
	 * 
	 * /* TEST1 - manager can create element
	 */

	/*
	 * @Test public void
	 * testCreateElementBoundaryByUserManagerAndCheckIfExistInTheDatabase()throws
	 * Exception{
	 * 
	 * 
	 * 
	 * 
	 * ElementBoundary elementBoundary = new ElementBoundary();
	 * elementBoundary.setName("ban"); elementBoundary.setActive(true);
	 * elementBoundary.setType("DEMO_ELEMENT");
	 * 
	 * ElementBoundary boundaryOnServer = this.restTemplate
	 * .postForObject(this.elementUrl + "/{managerDomain}/{managerEmail}",
	 * elementBoundary , ElementBoundary.class,
	 * manager.getUserId().getDomain(),manager.getUserId().getEmail());
	 * 
	 * this.restTemplate .getForObject(this.elementUrl +
	 * "/{userDomain}/{userEmail}/{elementDomain}/{elementID}",
	 * ElementBoundary.class,
	 * manager.getUserId().getDomain(),manager.getUserId().getEmail(),
	 * boundaryOnServer.getElementId().getDomain(),
	 * boundaryOnServer.getElementId().getId());
	 * 
	 * 
	 * }
	 * 
	 * 
	 * /* TEST2 - player can not create element
	 */
	/*
	 * @Test public void
	 * testCreateElementBoundaryByUserPlayerAndCheckIfExistInTheDatabase()throws
	 * Exception{
	 * 
	 * 
	 * 
	 * ElementBoundary elementBoundary = new ElementBoundary();
	 * elementBoundary.setName("ban"); elementBoundary.setActive(true);
	 * elementBoundary.setType("DEMO_ELEMENT"); try { ElementBoundary
	 * boundaryOnServer = this.restTemplate .postForObject(this.elementUrl +
	 * "/{managerDomain}/{managerEmail}", elementBoundary , ElementBoundary.class,
	 * player.getUserId().getDomain(),player.getUserId().getEmail()); fail(); }
	 * catch(HttpServerErrorException ex) {
	 * assertTrue(ex.getMessage().contains("Only manager can create element")); } }
	 * 
	 * 
	 * TEST3 - admin can not create element
	 * 
	 * @Test public void
	 * testCreateElementBoundaryByUserAdminAndCheckIfExistInTheDatabase()throws
	 * Exception{
	 * 
	 * 
	 * 
	 * ElementBoundary elementBoundary = new ElementBoundary();
	 * elementBoundary.setName("ban"); elementBoundary.setActive(true);
	 * elementBoundary.setType("DEMO_ELEMENT"); try { ElementBoundary
	 * boundaryOnServer = this.restTemplate .postForObject(this.elementUrl +
	 * "/{managerDomain}/{managerEmail}", elementBoundary , ElementBoundary.class,
	 * admin.getUserId().getDomain(),admin.getUserId().getEmail()); fail(); }
	 * catch(HttpServerErrorException ex) {
	 * assertTrue(ex.getMessage().contains("Only manager can create element")); } }
	 * 
	 * 
	 * TEST4 - update element name (manager user)
	 * 
	 * @Test public void
	 * testPutOfElementAndUpdateItsNameThenTheNameIsUpdatedInTheDataBaseByManager()
	 * throws Exception {
	 * 
	 * ElementBoundary elementBoundary = new ElementBoundary();
	 * elementBoundary.setName("ban"); elementBoundary.setActive(true);
	 * elementBoundary.setType("DEMO_ELEMENT");
	 * 
	 * ElementBoundary boundaryOnServer = this.restTemplate
	 * .postForObject(this.elementUrl + "/{managerDomain}/{managerEmail}",
	 * elementBoundary , ElementBoundary.class,
	 * manager.getUserId().getDomain(),manager.getUserId().getEmail()); String
	 * userDomain = boundaryOnServer.getCreatedBy().getUserId().getDomain(); String
	 * userEmail = boundaryOnServer.getCreatedBy().getUserId().getEmail(); String
	 * elementDomain = boundaryOnServer.getElementId().getDomain(); String elementId
	 * = boundaryOnServer.getElementId().getId();
	 * 
	 * ElementBoundary update = new ElementBoundary(); update.setActive(true);
	 * update.setName("workedd");
	 * 
	 * this.restTemplate.put( this.elementUrl +
	 * "/{userDomain}/{userEmail}/{elementDomain}/{elementID}" , update,
	 * userDomain,userEmail,elementDomain,elementId);
	 * 
	 * ElementBoundary updatedNameInElementBoundary = this.restTemplate
	 * .getForObject(this.elementUrl +
	 * "/{userDomain}/{userEmail}/{elementDomain}/{elementID}",
	 * ElementBoundary.class, userDomain,userEmail,elementDomain,elementId);
	 * 
	 * assertThat(updatedNameInElementBoundary.getName()).isNotNull().isEqualTo(
	 * update.getName());
	 * assertThat(updatedNameInElementBoundary.getName()).isNotNull().isNotEqualTo(
	 * elementBoundary.getName()); }
	 * 
	 * 
	 * TEST5 - can not update element name (player user)
	 * 
	 * @Test public void
	 * testPutOfElementAndUpdateItsNameThenTheNameIsUpdatedInTheDataBaseByPlayer()
	 * throws Exception {
	 * 
	 * ElementBoundary elementBoundary = new ElementBoundary();
	 * elementBoundary.setName("ban"); elementBoundary.setActive(true);
	 * elementBoundary.setType("DEMO_ELEMENT");
	 * 
	 * ElementBoundary boundaryOnServer = this.restTemplate
	 * .postForObject(this.elementUrl + "/{managerDomain}/{managerEmail}",
	 * elementBoundary , ElementBoundary.class,
	 * manager.getUserId().getDomain(),manager.getUserId().getEmail()); String
	 * userDomain =player.getUserId().getDomain(); String userEmail =
	 * player.getUserId().getEmail(); String elementDomain =
	 * boundaryOnServer.getElementId().getDomain(); String elementId =
	 * boundaryOnServer.getElementId().getId();
	 * 
	 * ElementBoundary update = new ElementBoundary(); update.setActive(true);
	 * update.setName("workedd"); try{ this.restTemplate.put( this.elementUrl +
	 * "/{userDomain}/{userEmail}/{elementDomain}/{elementID}" , update,
	 * userDomain,userEmail,elementDomain,elementId);
	 * 
	 * 
	 * fail(); } //THEN we got an exception catch(HttpServerErrorException ex) {
	 * assertTrue(ex.getMessage().contains("Only manager can update  element")); } }
	 * 
	 * 
	 * TEST6 - can not update element name (admin user)
	 * 
	 * @Test public void
	 * testPutOfElementAndUpdateItsNameThenTheNameIsUpdatedInTheDataBaseByAdmin()
	 * throws Exception {
	 * 
	 * ElementBoundary elementBoundary = new ElementBoundary();
	 * elementBoundary.setName("ban"); elementBoundary.setActive(true);
	 * elementBoundary.setType("DEMO_ELEMENT");
	 * 
	 * ElementBoundary boundaryOnServer = this.restTemplate
	 * .postForObject(this.elementUrl + "/{managerDomain}/{managerEmail}",
	 * elementBoundary , ElementBoundary.class,
	 * manager.getUserId().getDomain(),manager.getUserId().getEmail()); String
	 * userDomain =admin.getUserId().getDomain(); String userEmail =
	 * admin.getUserId().getEmail(); String elementDomain =
	 * boundaryOnServer.getElementId().getDomain(); String elementId =
	 * boundaryOnServer.getElementId().getId();
	 * 
	 * ElementBoundary update = new ElementBoundary(); update.setActive(true);
	 * update.setName("workedd"); try{ this.restTemplate.put( this.elementUrl +
	 * "/{userDomain}/{userEmail}/{elementDomain}/{elementID}" , update,
	 * userDomain,userEmail,elementDomain,elementId);
	 * 
	 * 
	 * fail(); } //THEN we got an exception catch(HttpServerErrorException ex) {
	 * assertTrue(ex.getMessage().contains("Only manager can update  element")); } }
	 * 
	 * 
	 * TEST7 - id after post and get is the same
	 * 
	 * @Test public void
	 * testPostNewElementThenTheDatabaseHasAnElementWithTheSameElementIdAsPosted() {
	 * 
	 * ElementBoundary elementBoundary = new ElementBoundary();
	 * elementBoundary.setName("ban"); elementBoundary.setActive(true);
	 * elementBoundary.setType("DEMO_ELEMENT");
	 * 
	 * 
	 * ElementBoundary newElementBoundary = this.restTemplate
	 * .postForObject(this.elementUrl + "/{managerDomain}/{managerEmail}",
	 * elementBoundary , ElementBoundary.class,
	 * manager.getUserId().getDomain(),manager.getUserId().getEmail());
	 * 
	 * ElementIdBoundary currentElementIdPosted = this.restTemplate
	 * .getForObject(this.elementUrl +
	 * "/{userDomain}/{userEmail}/{elementDomain}/{elementID}",
	 * ElementBoundary.class,
	 * newElementBoundary.getCreatedBy().getUserId().getDomain(),
	 * newElementBoundary.getCreatedBy().getUserId().getEmail(),
	 * newElementBoundary.getElementId().getDomain(),
	 * newElementBoundary.getElementId().getId()) .getElementId();
	 * 
	 * assertThat(currentElementIdPosted.getId()).isNotNull()
	 * .isEqualTo(newElementBoundary.getElementId().getId()); }
	 * 
	 * 
	 * 
	 * 
	 * @Test public void testUpdateInactiveElement()throws Exception{
	 * 
	 * //GIVEN database with inactive element
	 * 
	 * ElementBoundary elementBoundary = new ElementBoundary();
	 * elementBoundary.setName("ban"); elementBoundary.setActive(false);
	 * elementBoundary.setType("DEMO_ELEMENT");
	 * 
	 * ElementBoundary newElementBoundary = this.restTemplate
	 * .postForObject(this.elementUrl + "/{managerDomain}/{managerEmail}",
	 * elementBoundary , ElementBoundary.class, "test","te@st.com");
	 * 
	 * String userDomain =
	 * newElementBoundary.getCreatedBy().getUserId().getDomain(); String userEmail =
	 * newElementBoundary.getCreatedBy().getUserId().getEmail(); String
	 * elementDomain = newElementBoundary.getElementId().getDomain(); String
	 * elementId = newElementBoundary.getElementId().getId();
	 * 
	 * //WHEN we put an updated element try { ElementBoundary update = new
	 * ElementBoundary(); update.setCreatedBy(newElementBoundary.getCreatedBy());
	 * update.setName("workedd"); //updated name
	 * 
	 * this.restTemplate.put( this.elementUrl +
	 * "/{userDomain}/{userEmail}/{elementDomain}/{elementID}" , update,
	 * userDomain,userEmail,elementDomain,elementId);
	 * 
	 * fail();// when we reach this line thats mean the test failed } //THEN we got
	 * an exception catch(HttpServerErrorException ex) {
	 * assertTrue(ex.getMessage().contains("Element Not Active")); }
	 * 
	 * 
	 * } //TEST 8 create 5 element and check if exsist
	 * 
	 * @Test public void
	 * testPostFiveElementsAndCheckIfTheyExistInTheDataBase()throws Exception{ //
	 * GIVEN the server is up //AND the DB contains 5 elements
	 * 
	 * 
	 * UserBoundary managerBoundary = this.restTemplate .postForObject(this.userUrl,
	 * new NewUserDetails("naory@gmail.com","niv",":)","MANAGER"),
	 * UserBoundary.class);
	 * 
	 * List<ElementBoundary> AllElementsInDataBase =
	 * 
	 * IntStream.range(1,6) .mapToObj(i-> "test"+i) .map(userName -> new
	 * ElementBoundary (userName,null,"DEMO_ELEMENT",true,null,null,null,null))
	 * .map(boundary-> this.restTemplate .postForObject(this.elementUrl +
	 * "/{managerDomain}/{managerEmail}", boundary, ElementBoundary.class,
	 * managerBoundary.getUserId().getDomain(),managerBoundary.getUserId().getEmail(
	 * ))) .collect(Collectors.toList());
	 * 
	 * //WHEN we get all elements
	 * 
	 * ElementBoundary[] results = this.restTemplate .getForObject(this.elementUrl +
	 * "/{userDomain}/{userEmail}", ElementBoundary[].class,
	 * AllElementsInDataBase.get(0).getCreatedBy().getUserId().getDomain(),
	 * AllElementsInDataBase.get(0).getCreatedBy().getUserId().getEmail());
	 * 
	 * //THEN they exists in DB assertThat(results)
	 * .hasSize(AllElementsInDataBase.size())
	 * .usingRecursiveFieldByFieldElementComparator()
	 * .containsExactlyInAnyOrderElementsOf(AllElementsInDataBase);
	 * 
	 * }
	 * 
	 * 
	 * 
	 * //Test 9 create 3 elements and delete with user(ADMIN)
	 * 
	 * @Test public void
	 * testPostThreeElementsThenDeleteAllThemsWihtAdminUser()throws Exception{ //
	 * GIVEN the server is up //AND the DB contains 3 elements and an admin
	 * 
	 * UserBoundary adminBoundary = this.restTemplate .postForObject(this.userUrl,
	 * new NewUserDetails("te@sst.com","baan",":)","ADMIN"), UserBoundary.class);
	 * 
	 * UserBoundary managerBoundary = this.restTemplate .postForObject(this.userUrl,
	 * new NewUserDetails("naory@gmail.com","niv",":)","MANAGER"),
	 * UserBoundary.class);
	 * 
	 * List<ElementBoundary> AllElementsInDataBase = IntStream.range(1,4)
	 * .mapToObj(i-> "test"+i) .map(userName -> new ElementBoundary
	 * (userName,null,"DEMO_ELEMENT",true,null,null,null,null)) .map(boundary->
	 * this.restTemplate .postForObject(this.elementUrl +
	 * "/{managerDomain}/{managerEmail}", boundary, ElementBoundary.class,
	 * managerBoundary.getUserId().getDomain(),managerBoundary.getUserId().getEmail(
	 * ))) .collect(Collectors.toList());
	 * 
	 * //WHEN we delete all elements in DB this.restTemplate .delete(this.adminUrl +
	 * "/elements/{adminDomain}/{adminEmail}",
	 * adminBoundary.getUserId().getDomain(), adminBoundary.getUserId().getEmail());
	 * 
	 * //THEN the DB is empty
	 * 
	 * UserBoundary[] results = this.restTemplate. getForObject(this.elementUrl +
	 * "/{userDomain}/{userEmail}", UserBoundary[].class,
	 * AllElementsInDataBase.get(0).getCreatedBy().getUserId().getDomain(),
	 * AllElementsInDataBase.get(0).getCreatedBy().getUserId().getEmail ());
	 * 
	 * assertThat(results).isEmpty();
	 * 
	 * }
	 * 
	 * //Test 10 create an element with empty name and check if throw exception
	 * 
	 * @Test public void testPostAnElementWithEmptyNameAndThenThrowAnException()
	 * throws Exception { //GIVEN server is up
	 * 
	 * //WHEN i post an element with empty name ElementBoundary newElementPosted =
	 * new ElementBoundary(); newElementPosted.setActive(true);
	 * newElementPosted.setType("DEMO_ELEMENT"); newElementPosted.setName(""); //
	 * empty name try { this.restTemplate .postForObject(this.elementUrl +
	 * "/{managerDomain}/{managerEmail}" , newElementPosted, ElementBoundary.class,
	 * "test","te@st.com"); fail(); }
	 * 
	 * catch(HttpServerErrorException ex) { assertTrue(ex instanceof
	 * HttpServerErrorException); }
	 * 
	 * 
	 * } // Test 11 create an element with empty type and check if throw exception
	 * 
	 * @Test public void testPostAnElementWithEmptyTypeAndThenThrowAnException()
	 * throws Exception { //GIVEN server is up
	 * 
	 * UserBoundary managerBoundary = this.restTemplate .postForObject(this.userUrl,
	 * new NewUserDetails("naory@gmail.com","niv",":)","MANAGER"),
	 * UserBoundary.class);
	 * 
	 * 
	 * //WHEN i post an element with empty type ElementBoundary newElementPosted =
	 * new ElementBoundary(); newElementPosted.setActive(true);
	 * newElementPosted.setType(""); //empty type newElementPosted.setName("jhon");
	 * try { this.restTemplate .postForObject(this.elementUrl +
	 * "/{managerDomain}/{managerEmail}" , newElementPosted, ElementBoundary.class,
	 * managerBoundary.getUserId().getDomain(),managerBoundary.getUserId().getEmail(
	 * )); fail(); }
	 * 
	 * catch(HttpServerErrorException ex) { assertTrue(ex instanceof
	 * HttpServerErrorException); }
	 * 
	 * 
	 * }
	 */

	// Test 12 put 3 element Children of 1 origin ant save them in database
	@Test
	public void PutChildElementAndCheckIfTheyExsistInDataBase() {
		UserBoundary managerBoundary = this.restTemplate.postForObject(this.userUrl,
				new NewUserDetails("naory@gmail.com", "niv", ":)", "MANAGER"), UserBoundary.class);

		ElementBoundary elementBoundaryParent = new ElementBoundary();
		elementBoundaryParent.setName("ban");
		elementBoundaryParent.setActive(true);
		elementBoundaryParent.setType("DEMO_ELEMENT");

		ElementBoundary elementBoundaryChild1 = new ElementBoundary();
		elementBoundaryChild1.setName("ban");
		elementBoundaryChild1.setActive(true);
		elementBoundaryChild1.setType("DEMO_ELEMENT");
		

		ElementBoundary elementBoundaryChild2 = new ElementBoundary();
		elementBoundaryChild2.setName("ban");
		elementBoundaryChild2.setActive(true);
		elementBoundaryChild2.setType("DEMO_ELEMENT");

		ElementBoundary elementBoundaryChild3 = new ElementBoundary();
		elementBoundaryChild3.setName("ban");
		elementBoundaryChild3.setActive(true);
		elementBoundaryChild3.setType("DEMO_ELEMENT");

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

		
		
		///call to bind!!
		this.restTemplate.put(this.elementUrl + "/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}/children",
				boundaryOnServerChild1.getElementId() , this.manager.getUserId().getDomain(),
				this.manager.getUserId().getEmail(), boundaryOnServerParent.getElementId().getDomain(),
				boundaryOnServerParent.getElementId().getId());

		this.restTemplate.put(this.elementUrl + "/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}/children",
				boundaryOnServerChild2.getElementId() , this.manager.getUserId().getDomain(),
				this.manager.getUserId().getEmail(), boundaryOnServerParent.getElementId().getDomain(),
				boundaryOnServerParent.getElementId().getId());

		this.restTemplate.put(this.elementUrl + "/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}/children",
				boundaryOnServerChild3.getElementId() , this.manager.getUserId().getDomain(),
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
	/*
	 * public void
	 * putElementChildAndElementParentAndRetriveElementParentbyUserAndChild() {
	 * UserBoundary managerBoundary = this.restTemplate .postForObject(this.userUrl,
	 * new NewUserDetails("naory@gmail.com","niv",":)","MANAGER"),
	 * UserBoundary.class);
	 * 
	 * ElementBoundary elementBoundaryOrigin = new ElementBoundary();
	 * elementBoundaryOrigin.setName("ban"); elementBoundaryOrigin.setActive(true);
	 * elementBoundaryOrigin.setType("DEMO_ELEMENT");
	 * 
	 * 
	 * 
	 * 
	 * ElementBoundary elementBoundaryChild = new ElementBoundary();
	 * elementBoundaryChild.setName("niv"); elementBoundaryChild.setActive(true);
	 * elementBoundaryChild.setType("DEMO_ELEMENT");
	 * 
	 * 
	 * 
	 * 
	 * this.restTemplate .getForObject(this.elementUrl +
	 * "/{userDomain}/{userEmail}/{elementDomain}/{elementID}",
	 * ElementBoundary.class,
	 * managerBoundary.getUserId().getDomain(),managerBoundary.getUserId().getEmail(
	 * ),elementBoundaryChild1.getElementId().getDomain(),elementBoundaryChild1.
	 * getElementId().getId(),0,1);
	 * 
	 * 
	 * 
	 * }
	 */
}
