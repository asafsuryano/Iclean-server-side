package acs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.HttpServerErrorException;
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
		//GEVEN the server is up
		//AND the DB contains an user with roleAttribute : PLAYER
		NewUserDetails newUserDetails = new NewUserDetails("mola@be","tam", ":))", "PLAYER");

		UserBoundary boundaryOnServer =
				this.restTemplate
				.postForObject(this.userUrl, newUserDetails, UserBoundary.class);
			
		//WHEN i change the user's role to ADMIN
		User userId = boundaryOnServer.getUserId();
		UserBoundary updatedRole = new UserBoundary();
		updatedRole.setUserId(userId);
		updatedRole.setRole("ADMIN");
	
		this.restTemplate.put(this.userUrl + "/{userDomain}/{userEmail}",
				updatedRole,
				userId.getDomain(),userId.getEmail());
		
		//THEN the DB contains the user with the updated role
		assertThat(this.restTemplate
				.getForObject(this.userUrl + "/login/{userDomain}/{userEmail}",
						UserBoundary.class,
						userId.getDomain(),userId.getEmail()))
		.extracting("username","role")
		.containsExactly(boundaryOnServer.getUsername(),updatedRole.getRole());
		
	} 

	
	@Test
	public void testPostNewUserThenTheDatabaseHasAUserWithTheSameUserEmailAsPosted() throws Exception {
		//GEVEN the server is up
		
		//WHEN i post new user 
		UserBoundary newUserPosted =
				this.restTemplate
				.postForObject(this.userUrl,
						new NewUserDetails("nasas@beaar","sam", "((:))", "ADMIN"),
						UserBoundary.class);
		
		//THEN the DB contains the same userEmail
		User currentUserEmailPosted =
				this.restTemplate
				.getForObject(this.userUrl + "/login/{userDomain}/{userEmail}",
						UserBoundary.class, "2020b.ben.halfon",newUserPosted.getUserId().getEmail())
				.getUserId();
		
		assertThat(currentUserEmailPosted.getEmail()).isNotNull().isEqualTo(newUserPosted.getUserId().getEmail());
	}
	
	@Test
	public void testPostAnUserThatAlreadyExistInTheDataBaseThenThrowAnException() {
		
		//GEVEN the server is up
		//And the dataBase Contains User
	
		this.restTemplate
		.postForObject(this.userUrl,
				new NewUserDetails("nasas@ss","saam", ":)", "PLAYER"),
				UserBoundary.class);
		
		//When I put an user that already exist in DB
		try {
		this.restTemplate.postForObject(this.userUrl,
				new NewUserDetails("nasas@ss","saam", ":)", "PLAYER"),
				UserBoundary.class);
		fail();
		}
		//THEN will throw an exception that the user is already exist
		catch(HttpServerErrorException ex) {
			assertTrue(ex.getMessage().contains("User allready exsist "));
		}
	}
	
	@Test
	public void tsetPostANewUserWithInvalidRoleThenWeGetAnException(){
		
		//GEVEN the server is up
		
		//WHEN i put an user with invalid role
		try {
			this.restTemplate.postForObject(this.userUrl,
					new NewUserDetails("nasas@sss","saam", ":)", "magnom"),
					UserBoundary.class);	
			fail();
		}
		
		//THEN we get an exception
		catch(HttpServerErrorException ex) {
			assertTrue(ex.getMessage().contains("No enum constant acs.data.UserRoles.magnom"));
		}
	}
	
	
	@Test
	public void tsetPostANewUserWithACorrectRoleAttributeButOneCharacterIsNotInUpperCaseThenWeGetAnException(){
		
		//GEVEN the server is up
		
		//WHEN i put an user with incorrect role
		try {
			this.restTemplate.postForObject(this.userUrl,
					new NewUserDetails("asas@sss","saam", ":)", "PLAYEr"),
					UserBoundary.class);	
			fail();
		}
		
		//THEN we get an exception
		catch(HttpServerErrorException ex) {
			assertTrue(ex.getMessage().contains("No enum constant acs.data.UserRoles.PLAYEr"));
		}
	}
	
	@Test
	public void testPostAnUserWithEmptyEmailAndThatWillThrowAnException() {
		
	//GEVEN the server is up
		
		//WHEN i post an user without email
		try {
			NewUserDetails newUser = new NewUserDetails();
			newUser.setUsername("saam");
			newUser.setAvatar(":");
			newUser.setRole("PLAYER");
			this.restTemplate.postForObject(this.userUrl,
					newUser,
					UserBoundary.class);	
			fail();
		}
		
		//THEN we get an exception
		catch(HttpServerErrorException ex) {
			assertTrue(ex.getMessage().contains("email invalid"));
		}
	}
		
	@Test
	public void tsetPostUserWithEmptyNameAndThatWillThrowAnException() {
		
		//GEVEN the server is up
		
		//WHEN i post an user with empty name
		try {
			NewUserDetails newUser = new NewUserDetails("te@st","",":)","PLAYER");
			
			this.restTemplate.postForObject(this.userUrl,
					newUser,
					UserBoundary.class);	
			fail();
		}
		
		//THEN we get an exception
		catch(HttpServerErrorException ex) {
			assertTrue(ex.getMessage().contains("username invalid"));
		}
			
		
	}
	
	@Test
	public void testPostNewUserAndTryingToLoginWithInCorrectUserEmailAndThatWillThrowAnException() {
		//GEVEN the server is up
		//ANd the DB contains an user
		
		NewUserDetails newUser = new NewUserDetails("te@st","ban",":)","PLAYER");
		
		UserBoundary boundaryOnServer =
				this.restTemplate
				.postForObject(this.userUrl,
						newUser,
						UserBoundary.class);
		
		//WHEN he try to login with incorrect user email
		try {
			this.restTemplate
			.getForObject(this.userUrl + "/login/{userDomain}/{userEmail}",
					UserBoundary.class,
					boundaryOnServer.getUserId().getDomain(),
					"bla@bla"); //incorrect email
			fail();
		}
		
		//THEN he get an exception
		catch(HttpServerErrorException ex) {
			assertTrue(ex instanceof HttpServerErrorException);
		}
		
	}
	
	@Test
	public void testPostNewUserAndTryingToLoginWithInCorrectUserDomainAndThatWillThrowAnException() {
		//GEVEN the server is up
		//ANd the DB contains an user
		
		NewUserDetails newUser = new NewUserDetails("te@sst","baan",":)","PLAYER");
		
		UserBoundary boundaryOnServer =
				this.restTemplate
				.postForObject(this.userUrl,
						newUser,
						UserBoundary.class);
		
		//WHEN he try to login with incorrect user domain
		try {
			this.restTemplate
			.getForObject(this.userUrl + "/login/{userDomain}/{userEmail}",
					UserBoundary.class,
					"blabla",  //incorrect domain
					boundaryOnServer.getUserId().getEmail()); 
			fail();
		}
		
		//THEN he get an exception
		catch(HttpServerErrorException ex) {
			assertTrue(ex instanceof HttpServerErrorException);
		}
	}
	
	@Test
	public void tsetTryingToUpdateRoleOfUserThatDoesNotExistInTheDataBase() {
		
		//GEVEN the server is up
		
		//When i try to update user that doesn't exist
		try {
		this.restTemplate
		.put(this.userUrl + "/{userDomain}/{userEmail}",
				new NewUserDetails("tes@t","ban",":)","PLAYER"),
				"blabla","bla@bla");
		fail();
		}
		//THEN i get an exception  
		catch(HttpServerErrorException ex) {
			System.err.println(ex.getMessage());
			assertTrue(ex instanceof HttpServerErrorException);
			
		}
		
	}
	
	@Test
	public void testPostNewUserWithEmptyAvatarAndThatWillThrowAnException() {
		
		//GEVEN the server is up
		
		//When i put user with empty avatar
		try {
		this.restTemplate
		.postForObject(this.userUrl,
				new NewUserDetails("tssses@ttt","bbana","","PLAYER"), // empty avatar
				UserBoundary.class);
		fail();
		}
		//THEN i get an exception
		catch(HttpServerErrorException ex) {
			assertTrue(ex instanceof HttpServerErrorException);
		}
	}
		
	@Test
	public void testPostNewUserWithNullAvatarAttributeAndThatWillThrowAnException() {
		
	//GEVEN the server is up
		
		//When i put user with empty avatar
		try {
		this.restTemplate
		.postForObject(this.userUrl,
				new NewUserDetails("as@ba","bbabn",null,"PLAYER"), // null avatar
				UserBoundary.class);
		fail();
		}
		//THEN i get an exception
		catch(HttpServerErrorException ex) {
			assertTrue(ex instanceof HttpServerErrorException);
		}
		
	}
	
	
		
	
}
