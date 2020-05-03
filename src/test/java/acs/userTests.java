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


import acs.newUserDetailsBoundaryPackage.NewUserDetails;
import acs.usersBoundaryPackage.User;
import acs.usersBoundaryPackage.UserBoundary;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class userTests {

	private RestTemplate restTemplate;
	private String userUrl;
	private String adminUrl;
	private int port;
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		this.userUrl  ="http://localhost:" + this.port + "/acs/users";
		this.adminUrl = "http://localhost:" + this.port +"/acs/admin/users";
		
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
		
		//then delete all after every test
		this.restTemplate
		.delete(this.adminUrl + "/{adminDomain}/{adminEmail}",
				admin.getUserId().getDomain(),
				admin.getUserId().getEmail());
	}
	
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
		
		assertThat(currentUserEmailPosted.getEmail()).isNotNull()
			.isEqualTo(newUserPosted.getUserId().getEmail());
	}
	
	@Test
	public void testPostAnUserThatAlreadyExistInTheDataBaseThenThrowAnException() throws Exception {
		
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
	public void tsetPostANewUserWithInvalidRoleThenWeGetAnException() throws Exception {
		
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
	public void tsetPostANewUserWithACorrectRoleAttributeButOneCharacterIsNotInUpperCaseThenWeGetAnException() throws Exception {
		
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
	public void testPostAnUserWithEmptyEmailAndThatWillThrowAnException() throws Exception {
		
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
	public void tsetPostUserWithEmptyNameAndThatWillThrowAnException () throws Exception {
		
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
	public void testPostNewUserAndTryingToLoginWithInCorrectUserEmailAndThatWillThrowAnException () throws Exception {
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
	public void testPostNewUserAndTryingToLoginWithInCorrectUserDomainAndThatWillThrowAnException() throws Exception {
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
	public void tsetTryingToUpdateRoleOfUserThatDoesNotExistInTheDataBase() throws Exception {
		
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
	public void testPostNewUserWithEmptyAvatarAndThatWillThrowAnException() throws Exception {
		
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
	public void testPostNewUserWithNullAvatarAttributeAndThatWillThrowAnException() throws Exception {
		
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
	
	@Test
	public void testInitTheServerWithTwoUsersAndCheckThatTheyExistsInTheDataBase() throws Exception {
		
		//GEVEN the server is up
		//AND the DB contains 2 users 
		List<UserBoundary> AllUsersInDataBase =
		IntStream.range(1,3)
		.mapToObj(i->(i==2)?"ADMIN":"PLAYER")
		.map(userRole-> new NewUserDetails("te@st"+userRole,"dan"+userRole,":)",userRole))
		.map(boundary-> this.restTemplate
				.postForObject(this.userUrl,
						boundary,
						UserBoundary.class))
		.collect(Collectors.toList()); 
		
		UserBoundary[] results = 
				this.restTemplate.
				getForObject(this.adminUrl + "/{adminDomain}/{adminEmail}",
				UserBoundary[].class,
				AllUsersInDataBase.get(1).getUserId().getDomain(),
				AllUsersInDataBase.get(1).getUserId().getEmail());
		
		assertThat(results)
		.hasSize(AllUsersInDataBase.size())
		.usingRecursiveFieldByFieldElementComparator()
		.containsExactlyInAnyOrderElementsOf(AllUsersInDataBase);
	}
	
	@Test
	public void testInitTheServerWithTwoUsersAndWhenWeDeleteThemTheDataBaseIsEmpty() throws Exception {
		
		//GEVEN the server is up
		//AND the DB contains 2 users 
		List<UserBoundary> AllUsersInDataBase =
		IntStream.range(1,3)
		.mapToObj(i->(i==2)?"ADMIN":"PLAYER")
		.map(userRole-> new NewUserDetails("te@st"+userRole,"dan"+userRole,":)",userRole))
		.map(boundary-> this.restTemplate
				.postForObject(this.userUrl,
						boundary,
						UserBoundary.class))
		.collect(Collectors.toList()); 
		
		//WHEN i delete them
		this.restTemplate
		.delete(this.adminUrl + "/{adminDomain}/{adminEmail}",
				AllUsersInDataBase.get(1).getUserId().getDomain(),
				AllUsersInDataBase.get(1).getUserId().getEmail());
		
		//THEN the DB is empty
		//to check that its empty we post an admin to get the array of users in DB
		
		UserBoundary admin =
				this.restTemplate
				.postForObject(this.userUrl,
						new NewUserDetails("te@st","ban",":)","ADMIN"),
						UserBoundary.class);
		
		UserBoundary[] results = 
				this.restTemplate.
				getForObject(this.adminUrl + "/{adminDomain}/{adminEmail}",
				UserBoundary[].class,
				admin.getUserId().getDomain(),
				admin.getUserId().getEmail());
		
		
		
		assertThat(results)
		.hasSize(1)  // the DB contain just the admin that we post
		.usingRecursiveFieldByFieldElementComparator()
		.containsExactly(admin);
		
	}
		
	
	
}
