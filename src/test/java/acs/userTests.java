package acs;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import acs.newUserDetailsBoundaryPackage.NewUserDetails;
import acs.usersBoundaryPackage.User;
import acs.usersBoundaryPackage.UserBoundary;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class userTests {

	private RestTemplate restTemplate;
	private String userUrl;
	private int port;
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		this.userUrl =    "http://localhost:" + this.port + "/acs/users";
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
	
	@Test
	public void testPostAUserThatAlreadyExistInTheDataBase() {}
	
	
	
}
