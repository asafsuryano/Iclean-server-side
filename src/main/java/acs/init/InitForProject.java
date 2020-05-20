package acs.init;

import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import acs.data.UserRoles;
import acs.elementBoundaryPackage.CreatedBy;
import acs.elementBoundaryPackage.ElementBoundary;
import acs.elementBoundaryPackage.ElementIdBoundary;
import acs.elementBoundaryPackage.Location;
import acs.logic.ExtraActionService;
import acs.logic.ExtraElementsService;
import acs.logic.ExtraUserService;
import acs.usersBoundaryPackage.User;
import acs.usersBoundaryPackage.UserBoundary;


@Component
@Profile("manualTests")
public class InitForProject implements CommandLineRunner {
	private ExtraActionService actionService;
	private ExtraUserService userService;
	private ExtraElementsService elementService;

	@Autowired	
	public InitForProject(ExtraActionService actionService,ExtraUserService userService,ExtraElementsService elementService) {
		this.actionService=actionService;
		this.userService=userService;
		this.elementService=elementService;
	}
	@Override
	public void run(String... args) throws Exception {
		String domain="2020b.ben.halfon";
		
		//init users
		User userid1=new User(domain, "user1@gmail.com");
		UserBoundary user1=new UserBoundary(userid1,UserRoles.MANAGER.toString() ,"hello1", "hey1");
		
		User userid2=new User(domain, "user2@gmail.com");
		UserBoundary user2=new UserBoundary(userid1,UserRoles.MANAGER.toString() ,"hello2", "hey2");
		
		User userid3=new User(domain, "user3@gmail.com");
		UserBoundary user3=new UserBoundary(userid3,UserRoles.PLAYER.toString() ,"hello3", "hey3");
		//init elements
		ElementIdBoundary empty=new ElementIdBoundary();
		
		ElementBoundary element1=new ElementBoundary("niv",empty, "demo", true, new Date(), new CreatedBy( 
				new acs.elementBoundaryPackage.UserId(userid1.getDomain(),userid1.getEmail())),
				new Location(0.0,0.0), new HashMap<>());
		
		ElementBoundary element2=new ElementBoundary("asaf",empty, "demo", true, new Date(), new CreatedBy( 
				new acs.elementBoundaryPackage.UserId(userid1.getDomain(),userid1.getEmail())),
				new Location(1,1), new HashMap<>());
		 
		
		ElementBoundary element3=new ElementBoundary("omer",empty, "demo", false, new Date(), new CreatedBy( 
				new acs.elementBoundaryPackage.UserId(userid1.getDomain(),userid1.getEmail())),
				new Location(5,5), new HashMap<>());
		
		ElementBoundary element4=new ElementBoundary("ben",empty, "demo", true, new Date(), new CreatedBy( 
				new acs.elementBoundaryPackage.UserId(userid1.getDomain(),userid1.getEmail())),
				new Location(5,5), new HashMap<>());
	
	    ElementBoundary element5=new ElementBoundary("daniel",empty, "demo", false, new Date(), new CreatedBy( 
	 		  new acs.elementBoundaryPackage.UserId(userid1.getDomain(),userid1.getEmail())),
			  new Location(2.1,2.1), new HashMap<>());
	         
	    
	         ///create all users
	         userService.createUser(user1);
	         userService.createUser(user2);
	         userService.createUser(user3);
	         
	         
	         
	         //crate all elements
	         elementService.create(user1.getUserId().getDomain(),user1.getUserId().getEmail(), element1);
	         elementService.create(user1.getUserId().getDomain(),user1.getUserId().getEmail(), element2);
	         elementService.create(user1.getUserId().getDomain(),user1.getUserId().getEmail(), element3);
	         elementService.create(user1.getUserId().getDomain(),user1.getUserId().getEmail(), element4);
	         elementService.create(user1.getUserId().getDomain(),user1.getUserId().getEmail(), element5);
	         
	         
	         
	         //retrive all elemetn
	        // elementService.getAll(user2.getUserId().getDomain(), user2.getUserId().getEmail());

	          
	        	
	}
	
}
