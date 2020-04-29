package acs;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import acs.elementBoundaryPackage.CreatedBy;
import acs.elementBoundaryPackage.ElementBoundary;
import acs.elementBoundaryPackage.ElementId;
import acs.elementBoundaryPackage.UserId;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class elementTests {
	private RestTemplate restTemplate;
	private String elementUrl;
	private int port;
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		this.elementUrl = "http://localhost:" + this.port + "/acs/elements";
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
		//elementBoundary.setElementId(new ElementId(" "," "));
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
}
