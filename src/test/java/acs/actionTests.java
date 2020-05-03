package acs;


import java.sql.Date;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.*;
import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import acs.actionBoundaryPackage.ActionBoundary;
import acs.actionBoundaryPackage.Element;
import acs.actionBoundaryPackage.ElementId;
import acs.actionBoundaryPackage.InvokedBy;
import acs.actionBoundaryPackage.UserId;
import acs.newUserDetailsBoundaryPackage.NewUserDetails;
import acs.usersBoundaryPackage.UserBoundary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class actionTests {
	private RestTemplate restTemplate;
	private String adminUrl;
	private String actionUrl;
	private String userUrl;
	private int port;

	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		this.adminUrl = "http://localhost:" + this.port + "/acs/admin";
		this.actionUrl="http://localhost:" + this.port+ "/acs/actions";
		this.userUrl  ="http://localhost:" + this.port + "/acs/users";
		
	}
	
	@BeforeEach
	public void setup() {
		
	}
	
	@AfterEach
	public void tearDown() {
		
		
	}

	//not completed
	@Test
	public void testPostActionInDataBaseAndCheckIfItExistInTheDataBase() throws Exception {
	//GIVEN database
		
	//WHEN we put new action with 
	ActionBoundary newAtiontPosted = new ActionBoundary();
	newAtiontPosted.setElement(new Element(new ElementId("as","te@st")));
	newAtiontPosted.setInvokedBy(new InvokedBy(new UserId("ac","as@as")));
	newAtiontPosted.setType("ACTION_TYPE");
	
	ActionBoundary boundaryOnServer = 
			this.restTemplate
			.postForObject(this.actionUrl ,newAtiontPosted,
					ActionBoundary.class);
	
	//THEN it exist in the database
	NewUserDetails admin = new NewUserDetails("mola@be","tam", ":))", "ADMIN");
	
	UserBoundary adminBoundary =
			this.restTemplate.postForObject(this.userUrl,
					admin, UserBoundary.class);
	 
	ActionBoundary[] results =
			this.restTemplate.getForObject(adminUrl + "/actions/{adminDomain}/{adminEmail}",
					ActionBoundary[].class,
					adminBoundary.getUserId().getDomain(),
					adminBoundary.getUserId().getEmail());
		
	//THEN it exist in the database
	
	assertThat(results)
	.hasSize(1) 
	.usingRecursiveFieldByFieldElementComparator()
	.containsExactly(boundaryOnServer);
				
	}
	
	@Test
	public void testCreateActionAndTypeOfActionNotNull() throws Exception {
	//GIVEN database
		
		//WHEN we put new action with null type
		ActionBoundary newAtiontPosted = new ActionBoundary();
		newAtiontPosted.setElement(new Element(new ElementId("ass","te@st")));
		newAtiontPosted.setInvokedBy(new InvokedBy(new UserId("ac","as@as")));
		newAtiontPosted.setType(null); // null type

		try {
			this.restTemplate
			.postForObject(this.actionUrl ,newAtiontPosted,
					ActionBoundary.class);
			
			fail();
		}
		//THEN we got an exception
		catch(HttpServerErrorException ex) {
				assertTrue(ex instanceof HttpServerErrorException);
		}
		
	}
	
	
	
	
	

	/*@Test
	public void testCreateManyActionsWithDifferentActionId() throws Exception {

		// GIVEN the database is clean

		// WHEN we create multiple actions and store it in database


	}*/
	
	@Test
	public void testCreateActionAndDeleteAllThenTheDataBaseIsEmpty() throws Exception {
	
		// GIVEN  clear database
		

		// WHEN we create an action
		ActionBoundary newaAtiontPosted = new ActionBoundary();
		newaAtiontPosted.setType(""); // 
		
		
		// AND  delete all
		this.restTemplate
		.delete(this.adminUrl + "/{adminDomain}/{adminEmail}",
				newaAtiontPosted.getActionId().getId());
		// THEN the database now empty
		
			
		
			}
	
	
	@Test
	public void testCreateActionWitUniqueActionId() throws Exception{
		// GIVEN the database is clean
		
		// WHEN I create actions 
		ActionBoundary actionBoundary = new ActionBoundary();	
		actionBoundary.setType("");
		ActionBoundary newActionBoundary =
				this.restTemplate
				.postForObject(this.actionUrl,
						actionBoundary
						, ActionBoundary.class);
		
	}

	
		
	
}
	
	
	
	
	
	
	
	
	
	
	
	
	
