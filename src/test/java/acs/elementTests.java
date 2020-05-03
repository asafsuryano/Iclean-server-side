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
import acs.elementBoundaryPackage.ElementId;
import acs.newUserDetailsBoundaryPackage.NewUserDetails;
import acs.usersBoundaryPackage.UserBoundary;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class elementTests {
	private RestTemplate restTemplate;
	private String elementUrl;
	private String adminUrl;
	private String userUrl;
	private int port;
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		this.elementUrl = "http://localhost:" + this.port + "/acs/elements";
		this.adminUrl = "http://localhost:" + this.port +"/acs/admin";
		this.userUrl  ="http://localhost:" + this.port + "/acs/users";
		
	}
	
	@BeforeEach
	public void setup() {
		
	}
	
	@AfterEach
	public void tearDown() {
	// post admin
	UserBoundary admin = 
			this.restTemplate
			.postForObject(this.userUrl,
					new NewUserDetails("ba@na","dana",":)","ADMIN"),
					UserBoundary.class);
	
	//then delete all users/elements after every test
	this.restTemplate
	.delete(this.adminUrl + "/users/{adminDomain}/{adminEmail}",
			admin.getUserId().getDomain(),
			admin.getUserId().getEmail());
	
	this.restTemplate
	.delete(this.adminUrl + "/elements/{adminDomain}/{adminEmail}",
			admin.getUserId().getDomain(),
			admin.getUserId().getEmail());

	}

	
	@Test
	public void testPutOfElementAndUpdateItsNameThenTheNameIsUpdatedInTheDataBase() throws Exception {
		
		ElementBoundary elementBoundary = new ElementBoundary();		
		elementBoundary.setName("ban");
		elementBoundary.setActive(true);
		elementBoundary.setType("DEMO_ELEMENT");
		
		ElementBoundary boundaryOnServer =
				this.restTemplate
				.postForObject(this.elementUrl + "/{managerDomain}/{managerEmail}",
						elementBoundary
						, ElementBoundary.class,
						"a","b");
		String userDomain = boundaryOnServer.getCreatedby().getUserId().getUserdomain();
		String userEmail = boundaryOnServer.getCreatedby().getUserId().getUserEmail();
		String elementDomain = boundaryOnServer.getElementId().getElementDomain();
		String elementId = boundaryOnServer.getElementId().getElementId();
	
		ElementBoundary update = new ElementBoundary();
		update.setCreatedby(boundaryOnServer.getCreatedby());
		update.setActive(true);
		update.setName("workedd");
		
		this.restTemplate.put(
				this.elementUrl + "/{userDomain}/{userEmail}/{elementDomain}/{elementID}" ,
				update,
				userDomain,userEmail,elementDomain,elementId);
		
		ElementBoundary updatedNameInElementBoundary = this.restTemplate
				.getForObject(this.elementUrl + "/{userDomain}/{userEmail}/{elementDomain}/{elementID}",
						ElementBoundary.class,
						userDomain,userEmail,elementDomain,elementId);
		
		assertThat(updatedNameInElementBoundary.getName()).isNotNull().isEqualTo(update.getName());						
	}
	
	@Test
	public void testPostNewElementThenTheDatabaseHasAnElementWithTheSameElementIdAsPosted() { 
		
		ElementBoundary elementBoundary = new ElementBoundary();
		elementBoundary.setName("ban");
		elementBoundary.setActive(true);
		elementBoundary.setType("DEMO_ELEMENT");
		
		
		ElementBoundary newElementBoundary =
				this.restTemplate
				.postForObject(this.elementUrl + "/{managerDomain}/{managerEmail}",
						elementBoundary
						, ElementBoundary.class,
						"a","b");
		
		ElementId currentElementIdPosted =
				this.restTemplate
				.getForObject(this.elementUrl + "/{userDomain}/{userEmail}/{elementDomain}/{elementID}",
						ElementBoundary.class, 
						newElementBoundary.getCreatedby().getUserId().getUserdomain(),
						newElementBoundary.getCreatedby().getUserId().getUserEmail(),
						newElementBoundary.getElementId().getElementDomain(),
						newElementBoundary.getElementId().getElementId())
				.getElementId();
	
		assertThat(currentElementIdPosted.getElementId()).isNotNull()
					.isEqualTo(newElementBoundary.getElementId().getElementId());
	}

	@Test
	public void testGetSpecificElementWithEmptyDatabase()throws Exception{

		// GIVEN the server is up
		
		
		//WHEN we Get element when there are no elements in DB
		try {
			//trying to get element that not exist
			this.restTemplate
			.getForObject(this.elementUrl + "/{userDomain}/{userEmail}/{elementDomain}/{elementID}",
					ElementBoundary.class,
					"a","b","2020b.ben.halfon","11514");  
			
			fail();
		}
		//THEN we got an exception
		catch(HttpServerErrorException ex) {
			assertTrue(ex.getMessage().contains("Could no find Element"));
		}
	}
	
	@Test
	public void testUpdateInactiveElement()throws Exception{

		//GIVEN database with inactive element
		
		ElementBoundary elementBoundary = new ElementBoundary();
		elementBoundary.setName("ban");
		elementBoundary.setActive(false);
		elementBoundary.setType("DEMO_ELEMENT");
		
		ElementBoundary newElementBoundary =
				this.restTemplate
				.postForObject(this.elementUrl + "/{managerDomain}/{managerEmail}",
						elementBoundary
						, ElementBoundary.class,
						"a","b");
		
		String userDomain = newElementBoundary.getCreatedby().getUserId().getUserdomain();
		String userEmail = newElementBoundary.getCreatedby().getUserId().getUserEmail();
		String elementDomain = newElementBoundary.getElementId().getElementDomain();
		String elementId = newElementBoundary.getElementId().getElementId();
		
		//WHEN we put an updated element 
		try {
		ElementBoundary update = new ElementBoundary();
		update.setCreatedby(newElementBoundary.getCreatedby());
		update.setName("workedd"); //update name
		this.restTemplate.put(
				this.elementUrl + "/{userDomain}/{userEmail}/{elementDomain}/{elementID}" ,
				update,
				userDomain,userEmail,elementDomain,elementId);
		
		fail();// when we reach this line thats mean the test failed
		}
		//THEN we got an exception
		catch(HttpServerErrorException ex) {
			assertTrue(ex.getMessage().contains("Element Not Active"));
		}
		
		
	}
	
	@Test
	public void testPostFiveElementsAndCheckIfTheyExistInTheDataBase()throws Exception{
		// GIVEN the server is up
		//AND the DB contains 5 elements
		
		List<ElementBoundary> AllElementsInDataBase =
				IntStream.range(1,6)
				.mapToObj(i-> "test"+i)
				.map(userId -> new ElementBoundary
						("ban",null,"DEMO_ELEMENT",true,null,null,null,null))
				.map(boundary-> this.restTemplate
						.postForObject(this.elementUrl + "/{managerDomain}/{managerEmail}",
								boundary,
								ElementBoundary.class,
								"aaa","bbb"))
				.collect(Collectors.toList());
		
		//WHEN we get all elements
		
		ElementBoundary[] results =
				this.restTemplate
				.getForObject(this.elementUrl + "/{userDomain}/{userEmail}",
						ElementBoundary[].class,
						AllElementsInDataBase.get(0).getCreatedby().getUserId().getUserdomain(),
						AllElementsInDataBase.get(0).getCreatedby().getUserId().getUserEmail());
		
		//THEN they exists in DB
		assertThat(results)
		.hasSize(AllElementsInDataBase.size())
		.usingRecursiveFieldByFieldElementComparator()
		.containsExactlyInAnyOrderElementsOf(AllElementsInDataBase);
				
	}
	
	@Test
	public void testPostThreeElementsThenDeleteAllThemsWihtAdminUser()throws Exception{
		// GIVEN the server is up
		//AND the DB contains 3 elements and an admin
		
		UserBoundary adminBoundary =
		this.restTemplate
		.postForObject(this.userUrl,
				new NewUserDetails("te@sst","baan",":)","ADMIN"),
				UserBoundary.class);
				
		List<ElementBoundary> AllElementsInDataBase =
		IntStream.range(1,4)
		.mapToObj(i-> "test"+i)
		.map(userId -> new ElementBoundary
				("ban",null,"DEMO_ELEMENT",true,null,null,null,null))
						.map(boundary-> this.restTemplate
								.postForObject(this.elementUrl + "/{managerDomain}/{managerEmail}",
										boundary,
										ElementBoundary.class,
										"aaa","bbb"))
		.collect(Collectors.toList());
		
		//WHEN we delete all elements in DB
		this.restTemplate
		.delete(this.adminUrl + "/elements/{adminDomain}/{adminEmail}",
				adminBoundary.getUserId().getDomain(),
				adminBoundary.getUserId().getEmail());
		
		//THEN the DB is empty
				
		UserBoundary[] results = 
				this.restTemplate.
				getForObject(this.elementUrl + "/{userDomain}/{userEmail}",
						UserBoundary[].class,
						AllElementsInDataBase.get(0).getCreatedby().getUserId().getUserdomain(),
						AllElementsInDataBase.get(0).getCreatedby().getUserId().getUserEmail ());
		
		assertThat(results).isEmpty();  
		
	}
	
	@Test
	public void testPostAnElementWithEmptyNameAndThenThrowAnException() throws Exception {
	//GIVEN server is up
	
	//WHEN i post an element with empty name
	ElementBoundary newElementPosted = new ElementBoundary();
	newElementPosted.setActive(true);
	newElementPosted.setType("DEMO_ELEMENT");
	newElementPosted.setName(""); // empty name
	try {
		this.restTemplate
		.postForObject(this.elementUrl + "/{managerDomain}/{managerEmail}" ,
				newElementPosted,
				ElementBoundary.class,
				"a","b");
		fail();
	}
	
	catch(HttpServerErrorException ex) {
			assertTrue(ex instanceof HttpServerErrorException);
	}
	
	
	}
	
	@Test
	public void testPostAnElementWithEmptyTypeAndThenThrowAnException() throws Exception {
	//GIVEN server is up
	
	//WHEN i post an element with empty type
	ElementBoundary newElementPosted = new ElementBoundary();
	newElementPosted.setActive(true);
	newElementPosted.setType("");
	newElementPosted.setName("jhon"); // empty name
	try {
		this.restTemplate
		.postForObject(this.elementUrl + "/{managerDomain}/{managerEmail}" ,
				newElementPosted,
				ElementBoundary.class,
				"a","b");
		fail();
	}
	
	catch(HttpServerErrorException ex) {
			assertTrue(ex instanceof HttpServerErrorException);
	}
	
	
	}
	
	
	
	
	
}
