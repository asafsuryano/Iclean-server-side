
package acs;

import static org.assertj.core.api.Assertions.assertThat;

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
import org.springframework.web.client.RestTemplate;

import acs.data.elementEntityProperties.Type;
import acs.elementBoundaryPackage.CreatedBy;
import acs.elementBoundaryPackage.ElementBoundary;
import acs.elementBoundaryPackage.ElementId;
import acs.elementBoundaryPackage.UserId;
import acs.newUserDetailsBoundaryPackage.NewUserDetails;
import acs.usersBoundaryPackage.User;
import acs.usersBoundaryPackage.UserBoundary;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ApplicationTests {
	private RestTemplate restTemplate;
	private String userUrl;
	private String elementUrl;
	private String adminUrl;
	private int port;
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		this.userUrl =    "http://localhost:" + this.port + "/acs/users";
		this.elementUrl = "http://localhost:" + this.port + "/acs/elements";
		this.adminUrl = "http://localhost:" + this.port + "/acs/admin";
	}
	
	@BeforeEach
	public void setup() {
		
	}
//	@AfterEach
//	public void tearDown() {
//		this.restTemplate
//			.delete(this.userUrl);
//	}
	
	@Test
	public void testContext() {
		
	}
	
	// users Tests
	
	@Test
	public void testPutOfUserAndUpdateItsRoleToAdminThenTheRoleIsUpdatedInTheDatabase() throws Exception {
		
		NewUserDetails newUserDetails = new NewUserDetails("mola@be","tam", ":))", "PLAYER");

		UserBoundary boundaryOnServer =
				this.restTemplate
				.postForObject(this.userUrl, newUserDetails, UserBoundary.class);
				
		User userId = boundaryOnServer.getUserId();
		UserBoundary updatedRole = new UserBoundary();
		updatedRole.setUserId(userId);
		updatedRole.setRole("ADMIN");
	
		this.restTemplate.put(this.userUrl + "/{userDomain}/{userEmail}",
				updatedRole,
				userId.getDomain(),userId.getEmail());
		
		System.err.println(boundaryOnServer.getRole());
		assertThat(this.restTemplate
				.getForObject(this.userUrl + "/login/{userDomain}/{userEmail}",
						UserBoundary.class,
						userId.getDomain(),userId.getEmail()))
		.extracting("username","role")
		.containsExactly(boundaryOnServer.getUsername(),updatedRole.getRole());
		
	} 

	
	@Test
	public void testPostNewUserThenTheDatabaseHasAUserWithTheSameUserEmailAsPosted() throws Exception {
		
		NewUserDetails newUserDetails = new NewUserDetails("nasas@ber","sam", "((:))", "ADMIN");
		
		UserBoundary newUserPosted =
				this.restTemplate
				.postForObject(this.userUrl,
						new NewUserDetails("nasas@beaar","sam", "((:))", "ADMIN"),
						UserBoundary.class);
		
		//System.err.println(newUserPosted.getUserId().getEmail());
		
		User currentUserEmailPosted =
				this.restTemplate
				.getForObject(this.userUrl + "/login/{userDomain}/{userEmail}",
						UserBoundary.class, "2020b.ben.halfon",newUserPosted.getUserId().getEmail())
				.getUserId();
		
		assertThat(currentUserEmailPosted.getEmail()).isNotNull().isEqualTo(newUserPosted.getUserId().getEmail());
	}
	
	// elements tests
	
	@Test
	public void testPutOfElementAndUpdateItsNameThenTheNameIsUpdatedInTheDataBase() throws Exception {
		
		ElementBoundary elementBoundary = new ElementBoundary();		
		elementBoundary.setCreatedby(new CreatedBy(new UserId("kaba","sol@ba")));
		elementBoundary.setType("DEMO_ELEMENT");
		elementBoundary.setName("ban");
		elementBoundary.setActive(true);
		
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
		update.setElementId(boundaryOnServer.getElementId());
		update.setName("workedd");
		
		this.restTemplate.put(this.elementUrl + "/{userDomain}/{userEmail}/{elementDomain}/{elementID}" ,
				update,userDomain,userEmail,elementDomain,elementId);
		
		ElementBoundary updatedNameInElementBoundary = this.restTemplate
				.getForObject(this.elementUrl + "/{userDomain}/{userEmail}/{elementDomain}/{elementID}",
						ElementBoundary.class,
						userDomain,userEmail,elementDomain,elementId);
		System.err.println(updatedNameInElementBoundary.getType());
		
		assertThat(updatedNameInElementBoundary.getName()).isNotNull().isEqualTo(update.getName());						
	}
	
	@Test
	public void testPostNewElementThenTheDatabaseHasAnElementWithTheSameElementIdAsPosted() { 
		
		ElementBoundary elementBoundary = new ElementBoundary();	
		elementBoundary.setCreatedby(new CreatedBy(new UserId("kaba","sol@ba")));
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
	
	// action tests
	
	/*
	 * 
	 * 
	 * 
	 * */
	
	// admin tests
	
//	@Test
//	public void testInitTheServerWithThreeUsersAndCheckThatTheyExistsInTheDBThenWhenTheAdminDeleteThemTheDataBaseIsEmpty() {
//		
//		
//		List<UserBoundary> AllUsersInDataBase =
//		IntStream.range(1,4)
//		.mapToObj(i->"User ")
//		.map(user-> new UserBoundary(new User("2020b.demo","user@us.er"),"PLAYER", "Demo", ":)"))
//		.map(boundary-> this.restTemplate
//				.postForObject(this.userUrl,
//						boundary,
//						UserBoundary.class))
//		.collect(Collectors.toList()); 
//		
//		UserBoundary[] results = 
//				this.restTemplate.
//				getForObject(adminUrl + "/users/{adminDomain}/{adminEmail}",
//				UserBoundary[].class,
//				uriVariables);
//		
//		assertThat(results)
//		.hasSize(AllUsersInDataBase.size())
//		.usingRecursiveFieldByFieldElementComparator()
//		.containsExactlyInAnyOrderElementsOf(AllUsersInDataBase);
//	}
	
}
