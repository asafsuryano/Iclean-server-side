
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
import acs.elementBoundaryPackage.ElementIdBoundary;
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
