package acs;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import acs.actionBoundaryPackage.ActionBoundary;
import acs.actionBoundaryPackage.Element;
import acs.actionBoundaryPackage.ElementId;
import acs.actionBoundaryPackage.InvokedBy;
import acs.actionBoundaryPackage.UserId;
import acs.data.reportsAttributes.Report;
import acs.elementBoundaryPackage.ElementBoundary;
import acs.newUserDetailsBoundaryPackage.NewUserDetails;
import acs.usersBoundaryPackage.UserBoundary;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class actionTests {
	private RestTemplate restTemplate;
	private String adminUrl;
	private String actionUrl;
	private String userUrl;
	private int port;
	private UserBoundary admin;
	private UserBoundary manager;
	private UserBoundary player;
	private ElementBoundary elem;
	private String elementUrl;
	private String paginationUrl;

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
		this.elementUrl = "http://localhost:" + this.port + "/acs/elements";
		this.paginationUrl="?page={page}&size={size}";
	}

	@BeforeEach
	public void setup() {
		this.admin = this.restTemplate.postForObject(this.userUrl,
				new NewUserDetails("admini@na.com", "dana", ":)", "ADMIN"), UserBoundary.class);
		this.manager = this.restTemplate.postForObject(this.userUrl,
				new NewUserDetails("mana@new.com", "s1am", "((:))", "MANAGER"), UserBoundary.class);
		this.player = this.restTemplate.postForObject(this.userUrl,
				new NewUserDetails("playyy@new.com", "sa2m", "((:P))", "PLAYER"), UserBoundary.class);
		elem = new ElementBoundary();
		ElementBoundary elem1 = new ElementBoundary();
		elem1.setName("ban");
		elem1.setActive(true);
		elem1.setType("Trash");
		elem = this.restTemplate.postForObject(
				this.elementUrl + "/{managerDomain}/{managerEmail}", 
				elem1, 
				ElementBoundary.class,
				this.manager.getUserId().getDomain(), 
				this.manager.getUserId().getEmail());

	}

	@AfterEach
	public void tearDown() {
		// post admin
		UserBoundary admin = 
				this.restTemplate
				.postForObject(this.userUrl,
						new NewUserDetails("ba@na.com","dana",":)","ADMIN"),
						UserBoundary.class);
		//then delete all users/elements after every test
		
		this.restTemplate.delete(this.adminUrl + "/elements/{adminDomain}/{adminEmail}", admin.getUserId().getDomain(),
				admin.getUserId().getEmail());
		this.restTemplate.delete(this.adminUrl + "/actions/{adminDomain}/{adminEmail}", admin.getUserId().getDomain(),
				admin.getUserId().getEmail());

		this.restTemplate
		.delete(this.adminUrl + "/users/{adminDomain}/{adminEmail}",
				admin.getUserId().getDomain(),
				admin.getUserId().getEmail());
	}

//	//TEST 1 - create action by player and check if its exists
//	@Test
//	public void testPostActionInDataBaseAndCheckIfItExistInTheDataBase() throws Exception {
//		//GIVEN the server is up
//		// and there is a user with PLAYER role 
//
//		//WHEN we put new action with PLAYER user
//		ActionBoundary newActiontPosted = new ActionBoundary();
//		HashMap<String,Object> map = new HashMap<>();
//		map.put("cleanLevel", "2");
//		newActiontPosted.setActionAttributes(map);
//		newActiontPosted.setElement(new Element(new ElementId(elem.getElementId().getDomain(),elem.getElementId().getId())));
//		InvokedBy user = new InvokedBy();
//		user.setUserId(new UserId(this.player.getUserId().getDomain(),this.player.getUserId().getEmail()));
//		newActiontPosted.setInvokedBy(user);
//		newActiontPosted.setType("TEST_TYPE");
//
//		ActionBoundary boundaryOnServer = 
//				this.restTemplate
//				.postForObject(this.actionUrl ,
//						newActiontPosted,
//						ActionBoundary.class);
//
//		//THEN it exist in the database
//
//
//		ActionBoundary[] results =
//				this.restTemplate.getForObject(adminUrl + "/actions/{adminDomain}/{adminEmail}"+this.paginationUrl,
//						ActionBoundary[].class,
//						admin.getUserId().getDomain(),
//						admin.getUserId().getEmail(),0,3);
//
//		assertThat(results).hasSize(1);
//		assertThat(results[0].getElement().getElementId().getId())
//		.isEqualTo(elem.getElementId().getId());
//		assertThat(results[0].getInvokedBy().getUserId().getEmail())
//		.isEqualTo(player.getUserId().getEmail());
//		assertThat(results[0].getType())
//		.isEqualTo(newActiontPosted.getType());
//
//
//	}
//
//
//	//TEST 2 - try to create action by manager and fail
//	@Test
//	public void testPostActionInDataBaseAndCheckIfItExistInTheDataBaseByManager() throws Exception {
//		//GIVEN the server is up
//		// and there is a user with MANAGER role 
//
//		//WHEN we put new action with MANAGER user
//		ActionBoundary newActiontPosted = new ActionBoundary();
//		HashMap<String,Object> map = new HashMap<>();
//		map.put("cleanLevel", "2");
//		newActiontPosted.setActionAttributes(map);
//		newActiontPosted.setElement(new Element(new ElementId(elem.getElementId().getDomain(),elem.getElementId().getId())));
//		InvokedBy user = new InvokedBy();
//		user.setUserId(new UserId(this.manager.getUserId().getDomain(),this.manager.getUserId().getEmail()));
//		newActiontPosted.setInvokedBy(user);
//		newActiontPosted.setType("TEST_TYPE");
//		try {
//			ActionBoundary boundaryOnServer = 
//					this.restTemplate
//					.postForObject(this.actionUrl ,
//							newActiontPosted,
//							ActionBoundary.class);
//
//			fail();
//		}
//
//		//THEN will throw an exception that the user not player
//		catch(HttpServerErrorException ex) {
//			assertTrue(ex.getMessage().contains("the user is not a player"));
//		}
//
//
//	}
//
//
//	//TEST 3 - create 3 actions by player and check if all exists
//	@Test
//	public void testPostSomeActionInDataBaseAndCheckIfTheyExistInTheDataBase() throws Exception {
//		//GIVEN the server is up
//		// and there is a user with PLAYER role 
//
//		//WHEN we put new 3 action with PLAYER user
//		for (int i = 0; i < 3; i++) {
//
//
//			ActionBoundary newActiontPosted = new ActionBoundary();
//			HashMap<String,Object> map = new HashMap<>();
//			map.put("cleanLevel", "2");
//			newActiontPosted.setActionAttributes(map);
//			newActiontPosted.setElement(new Element(new ElementId(elem.getElementId().getDomain(),elem.getElementId().getId())));
//			InvokedBy user = new InvokedBy();
//			user.setUserId(new UserId(this.player.getUserId().getDomain(),this.player.getUserId().getEmail()));
//			newActiontPosted.setInvokedBy(user);
//			newActiontPosted.setType("TEST_TYPE");
//
//			ActionBoundary boundaryOnServer = 
//					this.restTemplate
//					.postForObject(this.actionUrl ,
//							newActiontPosted,
//							ActionBoundary.class);
//		}
//		//THEN it exist in the database
//
//
//		ActionBoundary[] results =
//				this.restTemplate.getForObject(adminUrl + "/actions/{adminDomain}/{adminEmail}",
//						ActionBoundary[].class,
//						admin.getUserId().getDomain(),
//						admin.getUserId().getEmail());
//		assertThat(results).hasSize(3);
//		for (int i = 0; i < 3; i++) {
//			
//			assertTrue(results[i].getElement().getElementId().getId().equals(elem.getElementId().getId()));
//			
//			assertThat(results[i].getInvokedBy().getUserId().getEmail())
//			.isEqualTo(player.getUserId().getEmail());
//			assertThat(results[i].getType())
//			.isEqualTo("TEST_TYPE");
//		}
//	}
//
//	//TEST 4 - create actions with missing data
//	@Test
//	public void testCreateActionAndTypeOfActionNotNull() throws Exception {
//		//GIVEN server is up an there is a player user
//
//		//WHEN we put new action with null type
//		ActionBoundary newAtiontPosted = new ActionBoundary();
//		newAtiontPosted.setElement(new Element(new ElementId(
//				elem.getElementId().getDomain(),
//				elem.getElementId().getId())));
//		newAtiontPosted.setInvokedBy(new InvokedBy(new UserId(this.player.getUserId().getDomain()
//				,this.player.getUserId().getEmail())));
//		newAtiontPosted.setType(null); // null type
//
//		try {
//			this.restTemplate
//			.postForObject(this.actionUrl ,newAtiontPosted,
//					ActionBoundary.class);
//
//			fail();
//		}
//		//THEN we got an exception
//		catch(HttpServerErrorException ex) {
//			assertTrue(ex instanceof HttpServerErrorException);
//		}
//
//	}
//
//
//	//TEST 5 - create 4 actions by player get pagging of 2
//	@Test
//	public void testPostSomeActionInDataBaseAndCheckIfTheyExistInTheDataBasePagging() throws Exception {
//		//GIVEN the server is up
//		// and there is a user with PLAYER role 
//
//		//WHEN we put new 3 action with PLAYER user
//		for (int i = 0; i < 3; i++) {
//
//
//			ActionBoundary newActiontPosted = new ActionBoundary();
//			HashMap<String,Object> map = new HashMap<>();
//			map.put("cleanLevel", "2");
//			newActiontPosted.setActionAttributes(map);
//			newActiontPosted.setElement(new Element(new ElementId(elem.getElementId().getDomain(),elem.getElementId().getId())));
//			InvokedBy user = new InvokedBy();
//			user.setUserId(new UserId(this.player.getUserId().getDomain(),this.player.getUserId().getEmail()));
//			newActiontPosted.setInvokedBy(user);
//			newActiontPosted.setType("TEST_TYPE");
//
//			ActionBoundary boundaryOnServer = 
//					this.restTemplate
//					.postForObject(this.actionUrl ,
//							newActiontPosted,
//							ActionBoundary.class);
//		}
//		//THEN it exist in the database
//
//
//		ActionBoundary[] results =
//				this.restTemplate.getForObject(adminUrl + "/actions/{adminDomain}/{adminEmail}"+this.paginationUrl,
//						ActionBoundary[].class,
//						admin.getUserId().getDomain(),
//						admin.getUserId().getEmail(),0,2);
//		assertThat(results).hasSize(2);
//		for (int i = 0; i < 2; i++) {
//			assertThat(results[i].getElement().getElementId().getId())
//			.isEqualTo(elem.getElementId().getId());
//			assertThat(results[i].getInvokedBy().getUserId().getEmail())
//			.isEqualTo(player.getUserId().getEmail());
//			assertThat(results[i].getType())
//			.isEqualTo("TEST_TYPE");
//		}
//	}
//	
	//TEST 6 - create 4 actions by player get pagging of 2
	@Test
	public void testAddReportAction() {
		Report r = new Report();
		r.setComment("ben is the best");
		r.setTrashLevel(4);
		HashMap<String,Object> map = new HashMap<String, Object>();
		map.put("report", r);
		
		ActionBoundary newActiontPosted = new ActionBoundary();
		newActiontPosted.setType("addReport");
		newActiontPosted.setInvokedBy(new InvokedBy(new UserId(this.player.getUserId().getDomain(), 
				this.player.getUserId().getEmail())));
		newActiontPosted.setElement(new Element(
				new ElementId(elem.getElementId().getDomain(),
						elem.getElementId().getId())));
		newActiontPosted.setActionAttributes(map);
		
		ActionBoundary boundaryOnServer = 
				this.restTemplate
				.postForObject(this.actionUrl,
						newActiontPosted,
						ActionBoundary.class);
		
		
		ElementBoundary[] elementAfterAction=this.restTemplate.getForObject(this.elementUrl
				+"/{userDomain}/{userEmail}", ElementBoundary[].class,this.player.getUserId().getDomain(),
				this.player.getUserId().getEmail());
		ArrayList<Map<String,Object>> allReports=(ArrayList<Map<String,Object>>)elementAfterAction[0].getElementAttributes().get("reports");
		assertTrue(allReports.get(0).get("comment").equals(r.getComment()));
		
	}
	

	
	//TEST 7 - create action that adds a report and then create an action that cleans
	@Test
	public void testAddReportActionAndCleanAction() {
		Report r = new Report();
		r.setComment("ben is the best");
		r.setTrashLevel(4);
		HashMap<String,Object> map = new HashMap<String, Object>();
		map.put("report", r);
		
		ActionBoundary newActiontPosted = new ActionBoundary();
		newActiontPosted.setType("addReport");
		newActiontPosted.setInvokedBy(new InvokedBy(new UserId(this.player.getUserId().getDomain(), 
				this.player.getUserId().getEmail())));
		newActiontPosted.setElement(new Element(
				new ElementId(elem.getElementId().getDomain(),
						elem.getElementId().getId())));
		newActiontPosted.setActionAttributes(map);
		
		ActionBoundary boundaryOnServer = 
				this.restTemplate
				.postForObject(this.actionUrl ,
						newActiontPosted,
						ActionBoundary.class);
		
		ActionBoundary newActiontPosted1 = new ActionBoundary();
		newActiontPosted1.setType("cleanReports");
		newActiontPosted1.setInvokedBy(new InvokedBy(new UserId(this.player.getUserId().getDomain(), 
				this.player.getUserId().getEmail())));
		newActiontPosted1.setElement(new Element(
				new ElementId(elem.getElementId().getDomain(),
						elem.getElementId().getId())));
		
		ActionBoundary boundaryOnServer2 = 
				this.restTemplate
				.postForObject(this.actionUrl ,
						newActiontPosted1,
						ActionBoundary.class);
		
		ElementBoundary[] elementAfterAction=this.restTemplate.getForObject(this.elementUrl
				+"/{userDomain}/{userEmail}", ElementBoundary[].class,this.player.getUserId().getDomain(),
				this.player.getUserId().getEmail());
		ObjectMapper mapper=new ObjectMapper();
		ArrayList<Report> reports=mapper.convertValue(elementAfterAction[0].getElementAttributes().get("reportsArchive"),
				new TypeReference<ArrayList<Report>>() {});
		assertThat(reports).hasSize(2);
	}


	//TEST 8 - create action that adds a report and then create an action that cleans and then add another report
	@Test
	public void testAddReportActionAndCleanActionAndAddAnotherReport() {
		Report r = new Report();
		r.setComment("ben is the best");
		r.setTrashLevel(4);
		HashMap<String,Object> map = new HashMap<String, Object>();
		map.put("report", r);
		
		ActionBoundary newActiontPosted = new ActionBoundary();
		newActiontPosted.setType("addReport");
		newActiontPosted.setInvokedBy(new InvokedBy(new UserId(this.player.getUserId().getDomain(), 
				this.player.getUserId().getEmail())));
		newActiontPosted.setElement(new Element(
				new ElementId(elem.getElementId().getDomain(),
						elem.getElementId().getId())));
		newActiontPosted.setActionAttributes(map);
		
		ActionBoundary boundaryOnServer = 
				this.restTemplate
				.postForObject(this.actionUrl ,
						newActiontPosted,
						ActionBoundary.class);
		
		ActionBoundary newActiontPosted1 = new ActionBoundary();
		newActiontPosted1.setType("cleanReports");
		newActiontPosted1.setInvokedBy(new InvokedBy(new UserId(this.player.getUserId().getDomain(), 
				this.player.getUserId().getEmail())));
		newActiontPosted1.setElement(new Element(
				new ElementId(elem.getElementId().getDomain(),
						elem.getElementId().getId())));
		
		ActionBoundary boundaryOnServer2 = 
				this.restTemplate
				.postForObject(this.actionUrl ,
						newActiontPosted1,
						ActionBoundary.class);
		
		Report r3=new Report();
		r3.setComment("hello is the best");
		r3.setTrashLevel(3);
		HashMap<String,Object> map2 = new HashMap<String, Object>();
		map2.put("report", r3);
		
		ActionBoundary newActiontPosted2 = new ActionBoundary();
		newActiontPosted2.setType("addReport");
		newActiontPosted2.setInvokedBy(new InvokedBy(new UserId(this.player.getUserId().getDomain(), 
				this.player.getUserId().getEmail())));
		newActiontPosted2.setElement(new Element(
				new ElementId(elem.getElementId().getDomain(),
						elem.getElementId().getId())));
		newActiontPosted2.setActionAttributes(map2);
		
		ActionBoundary boundaryOnServer3 = 
				this.restTemplate
				.postForObject(this.actionUrl ,
						newActiontPosted2,
						ActionBoundary.class);
		
		ElementBoundary[] elementAfterAction=this.restTemplate.getForObject(this.elementUrl
				+"/{userDomain}/{userEmail}", ElementBoundary[].class,this.player.getUserId().getDomain(),
				this.player.getUserId().getEmail());
		System.out.println("");
	}
	
	//TEST 9 - create 3 actions that add 3 reports
	@Test
	public void testAdd3Reports() {
		Report r1 = new Report();
		r1.setComment("ben is the best");
		r1.setTrashLevel(4);
		HashMap<String,Object> map1 = new HashMap<String, Object>();
		map1.put("report", r1);
		
		ActionBoundary newActiontPosted1 = new ActionBoundary();
		newActiontPosted1.setType("addReport");
		newActiontPosted1.setInvokedBy(new InvokedBy(new UserId(this.player.getUserId().getDomain(), 
				this.player.getUserId().getEmail())));
		newActiontPosted1.setElement(new Element(
				new ElementId(elem.getElementId().getDomain(),
						elem.getElementId().getId())));
		newActiontPosted1.setActionAttributes(map1);
		
		ActionBoundary boundaryOnServer = 
				this.restTemplate
				.postForObject(this.actionUrl ,
						newActiontPosted1,
						ActionBoundary.class);
		
		Report r2 = new Report();
		r2.setComment("asaf is the best");
		r2.setTrashLevel(4);
		HashMap<String,Object> map2 = new HashMap<String, Object>();
		map2.put("report", r2);
		
		ActionBoundary newActiontPosted2 = new ActionBoundary();
		newActiontPosted2.setType("addReport");
		newActiontPosted2.setInvokedBy(new InvokedBy(new UserId(this.player.getUserId().getDomain(), 
				this.player.getUserId().getEmail())));
		newActiontPosted2.setElement(new Element(
				new ElementId(elem.getElementId().getDomain(),
						elem.getElementId().getId())));
		newActiontPosted2.setActionAttributes(map2);

		
		
		ActionBoundary boundaryOnServer2 = 
				this.restTemplate
				.postForObject(this.actionUrl ,
						newActiontPosted2,
						ActionBoundary.class);
		
		Report r3=new Report();
		r3.setComment("hello is the best");
		r3.setTrashLevel(3);
		HashMap<String,Object> map3 = new HashMap<String, Object>();
		map3.put("report", r3);
		
		ActionBoundary newActiontPosted3 = new ActionBoundary();
		newActiontPosted3.setType("addReport");
		newActiontPosted3.setInvokedBy(new InvokedBy(new UserId(this.player.getUserId().getDomain(), 
				this.player.getUserId().getEmail())));
		newActiontPosted3.setElement(new Element(
				new ElementId(elem.getElementId().getDomain(),
						elem.getElementId().getId())));
		newActiontPosted3.setActionAttributes(map3);
		
		ActionBoundary boundaryOnServer3 = 
				this.restTemplate
				.postForObject(this.actionUrl ,
						newActiontPosted3,
						ActionBoundary.class);
		
		ElementBoundary[] elementAfterAction=this.restTemplate.getForObject(this.elementUrl
				+"/{userDomain}/{userEmail}", ElementBoundary[].class,this.player.getUserId().getDomain(),
				this.player.getUserId().getEmail());
		System.out.println("");
	}
	
}













