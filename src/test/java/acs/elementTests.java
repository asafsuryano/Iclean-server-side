package acs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import acs.data.ElementEntity;
import acs.elementBoundaryPackage.CreatedBy;
import acs.elementBoundaryPackage.ElementBoundary;
import acs.elementBoundaryPackage.ElementId;
import acs.elementBoundaryPackage.UserId;
import acs.newUserDetailsBoundaryPackage.NewUserDetails;
import acs.serviceImplementation.elementServicePackage.ElementServiceImplementation;
import acs.usersBoundaryPackage.UserBoundary;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class elementTests {
	private RestTemplate restTemplate;
	private String elementUrl;
	private int port;
	private ElementServiceImplementation elementservice;
	private String adminUrl;
	private String userUrl;
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		this.elementUrl = "http://localhost:" + this.port + "/acs/elements";
		this.adminUrl = "http://localhost:" + this.port +"/acs/admin/users";
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
	
	//then delete all after every test
	this.restTemplate
	.delete(this.adminUrl + "/{adminDomain}/{adminEmail}",
			admin.getUserId().getDomain(),
			admin.getUserId().getEmail());
	}

	
	@Test
	public void testPutOfElementAndUpdateItsNameThenTheNameIsUpdatedInTheDataBase() throws Exception {
		
		ElementBoundary elementBoundary = new ElementBoundary();		
		elementBoundary.setCreatedby(new CreatedBy(new UserId("c","sol@ba")));
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
		elementBoundary.setCreatedby(new CreatedBy(new UserId("kaba","sol@ba")));
		elementBoundary.setActive(true);
		
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

		// GIVEN the database is clean
		//WHEN we Get element when there are no elements in DB
		assertThrows(Exception.class, () ->this.restTemplate.getForObject(this.elementUrl+ "/{userDomain}/{userEmail}/{elementDomain}/{elementID}",
				ElementBoundary.class,"userDomain","userEmail","a","b"));
		
	}
	
	
	

	
	@Test
	public void testCreateManyElementsWithTheSameUserIdAndCheckIfJustOneStoredInTheDataBase()throws Exception{
		//GIVEN clear database
		// WHEN I create number Of Users

	//	assertThat(actualUsers).is(numberOfUsers);
	
		
		
		
		// THEN the list Of Element is stored with size 1 
	
		
	}
	
	
	
	@Test
	public void testGetAllElementsWithEmptyDatabase()throws Exception{
		// GIVEN the database is clean
		//WHEN we Get all elements when there are no elements in DB
		
		
		
		
		
	}
	

	@Test
	public void testCreateElementAndCheckIfTypeOfElementNotNull() throws Exception {
	//GIVEN database is empty
		//WHEN 
		
	
		
	}
	
	@Test
	public void testGetOnlyActiveElements()throws Exception{
		
	}
	

	@Test
	public void testUpdateInactiveElement()throws Exception{
		//GIVEN database with inactive element
		
		ElementBoundary elementBoundary = new ElementBoundary();
		elementBoundary.setCreatedby(new CreatedBy(new UserId("kaba","sol@ba")));
		elementBoundary.setActive(false);
		
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
	public void testUpdateCertainPropertiesInElement()throws Exception{
		
	}
	
	@Test
	public void testDeleteAllElementsWihtAdminUser()throws Exception{
		
	}
	
}
