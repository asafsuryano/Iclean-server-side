package acs;


import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import acs.actionBoundaryPackage.ActionBoundary;
import acs.actionBoundaryPackage.Element;
import acs.actionBoundaryPackage.ElementId;
import acs.actionBoundaryPackage.InvokedBy;
import acs.actionBoundaryPackage.UserId;
import acs.elementBoundaryPackage.ElementBoundary;
import acs.newUserDetailsBoundaryPackage.NewUserDetails;
import acs.usersBoundaryPackage.UserBoundary;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class adminTests {
	private RestTemplate restTemplate;
	private UserBoundary admin;
	private UserBoundary manager;
	private UserBoundary player;
	private String elementUrl;
	private String adminUrl;
	private String userUrl;
	private String actionUrl;
	private String paginationUrl;
	private int port;
	
	
	
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		this.elementUrl = "http://localhost:" + this.port + "/acs/elements";
		this.adminUrl = "http://localhost:" + this.port + "/acs/admin";
		this.userUrl = "http://localhost:" + this.port + "/acs/users";
		this.actionUrl="http://localhost:" + this.port + "/acs/actions";
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
		
	

	}

	@AfterEach
	public void tearDown() {
		// // post admin
		 UserBoundary admin1 =
		 this.restTemplate
		 .postForObject(this.userUrl,
		 new NewUserDetails("ba@na.com","dana",":)","ADMIN"),
		 UserBoundary.class);
		
		// then delete all users/elements after every test

		this.restTemplate.delete(this.adminUrl + "/actions/{adminDomain}/{adminEmail}", admin.getUserId().getDomain(),
				admin.getUserId().getEmail());
		
		this.restTemplate.delete(this.adminUrl + "/elements/{adminDomain}/{adminEmail}", admin.getUserId().getDomain(),
				admin.getUserId().getEmail());
		
		this.restTemplate.delete(this.adminUrl + "/users/{adminDomain}/{adminEmail}", admin.getUserId().getDomain(),
				admin.getUserId().getEmail());

	}
	
	//Test 1 export all users by Admin user
	@Test
	public void  put3usersAndExportAllUsersByUserAdmin() {
		
	   this.restTemplate.postForObject(this.userUrl,
				new NewUserDetails("niv@na.com", "niv", ":)", "MANAGER"), UserBoundary.class);
		
	   this.restTemplate.postForObject(this.userUrl,
				new NewUserDetails("niv1@na.com", "elad", ":)", "PLAYER"), UserBoundary.class);
		
	   this.restTemplate.postForObject(this.userUrl,
				new NewUserDetails("niv2@na.com", "omer", ":)", "PLAYER"), UserBoundary.class);
	
		
	       UserBoundary[] User=this.restTemplate.getForObject(this.adminUrl + "/users" +"/{adminDomain}/{adminEmail}"+this.paginationUrl ,
				UserBoundary[].class, 
				this.admin.getUserId().getDomain(),this.admin.getUserId().getEmail(),0,3);
	       
	       assertThat(User).hasSize(3);
		
		
	} 
	
	
	//Test 2 export all action by Admin user
		@Test
		public void  put3ActionAndExportAllActionsByUserAdmin() {
			
			ElementBoundary elementBoundary1 = new ElementBoundary();
			elementBoundary1.setName("ban");
			elementBoundary1.setActive(true);
			elementBoundary1.setType("DEMO_ELEMENT");
			
			
		       ElementBoundary boundaryOnServer1 = this.restTemplate.postForObject(
					this.elementUrl + "/{managerDomain}/{managerEmail}", elementBoundary1, ElementBoundary.class,
					this.manager.getUserId().getDomain(), this.manager.getUserId().getEmail());

			//post element
			
            ActionBoundary	action1=new ActionBoundary();
            action1.setType("cleanReports");
            action1.setElement(new Element(new ElementId(
            boundaryOnServer1.getElementId().getDomain(),boundaryOnServer1.getElementId().getId())));
            action1.setInvokedBy(new InvokedBy(new UserId(this.player.getUserId().getDomain(),
            	this.player.getUserId().getEmail())));
            
            
            
            ActionBoundary	action2=new ActionBoundary();
            action2.setType("cleanReports");
            action2.setElement(new Element(new ElementId(
            boundaryOnServer1.getElementId().getDomain(),boundaryOnServer1.getElementId().getId())));   
            action2.setInvokedBy(new InvokedBy(new UserId(this.player.getUserId().getDomain(),
                	this.player.getUserId().getEmail())));
            
            ActionBoundary	action3=new ActionBoundary();
            action3.setType("cleanReports");
            action3.setElement(new Element(new ElementId(
            boundaryOnServer1.getElementId().getDomain(),boundaryOnServer1.getElementId().getId())));           
            action3.setInvokedBy(new InvokedBy(new UserId(this.player.getUserId().getDomain(),
                	this.player.getUserId().getEmail())));
			
		   this.restTemplate.postForObject(this.actionUrl,
					action1,ActionBoundary.class);
			
			
		   this.restTemplate.postForObject(this.actionUrl,
					action2,ActionBoundary.class);
		   
		   this.restTemplate.postForObject(this.actionUrl,
				   action3,ActionBoundary.class);
		   
		   
			
		       UserBoundary[] User=this.restTemplate.getForObject(this.adminUrl + "/actions" +"/{adminDomain}/{adminEmail}" +this.paginationUrl,
					UserBoundary[].class, 
					this.admin.getUserId().getDomain(),this.admin.getUserId().getEmail(),0,3);
		       
		       assertThat(User).hasSize(3);
			
			
		} 
	
	
	
}
 