package acs.init;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import acs.actionBoundaryPackage.ActionBoundary;
import acs.actionBoundaryPackage.Element;
import acs.actionBoundaryPackage.ElementId;
import acs.actionBoundaryPackage.InvokedBy;
import acs.actionBoundaryPackage.UserId;
import acs.data.UserRoles;
import acs.data.reportsAttributes.Report;
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
		UserBoundary user1=new UserBoundary(userid1,UserRoles.MANAGER.toString() ,"Shlomi", "robot");
		
//		User userid2=new User(domain, "user2@gmail.com");
//		UserBoundary user2=new UserBoundary(userid2,UserRoles.ADMIN.toString() ,"hello2", "hey2");
//		
		User userid3=new User(domain, "user3@gmail.com");
		UserBoundary user3=new UserBoundary(userid3,UserRoles.PLAYER.toString() ,"Yaakov", "angry");
		//init elements
		ElementIdBoundary empty=new ElementIdBoundary();
		
		HashMap<String,Object> el1Attr = new HashMap<String, Object>();
		el1Attr.put("description", "Yrakon park is a large park with many trashes");
		
		ElementBoundary element1=new ElementBoundary("Trash #5, Yarkon Park",empty, "trash_location", true, new Date(), new CreatedBy( 
				new acs.elementBoundaryPackage.UserId(userid1.getDomain(),userid1.getEmail())),
				new Location(0.0,0.0),el1Attr);
		
		HashMap<String,Object> el2Attr = new HashMap<String, Object>();
		el2Attr.put("description", "Bugrashov is a beach with large movement of people");
		
		ElementBoundary element2=new ElementBoundary("Bugrashov Beach",empty, "sea_location", true, new Date(), new CreatedBy( 
				new acs.elementBoundaryPackage.UserId(userid1.getDomain(),userid1.getEmail())),
				new Location(1,1), el2Attr);
		 
		HashMap<String,Object> el3Attr = new HashMap<String, Object>();
		el3Attr.put("description", "This is a quite street which is located near the beach.");
		
		ElementBoundary element3=new ElementBoundary("Lassalle 9a",empty, "trash_location", false, new Date(), new CreatedBy( 
				new acs.elementBoundaryPackage.UserId(userid1.getDomain(),userid1.getEmail())),
				new Location(5,5), el3Attr);
		
		ElementBoundary element4=new ElementBoundary("Rabin's Square",empty, "trash_location", true, new Date(), new CreatedBy( 
				new acs.elementBoundaryPackage.UserId(userid1.getDomain(),userid1.getEmail())),
				new Location(5,5), new HashMap<>());
	
	    ElementBoundary element5=new ElementBoundary("Meir Park",empty, "park_location", false, new Date(), new CreatedBy( 
	 		  new acs.elementBoundaryPackage.UserId(userid1.getDomain(),userid1.getEmail())),
			  new Location(2.1,2.1), new HashMap<>());
	         
	    HashMap<String,Object> el4Attr = new HashMap<String, Object>();
	    el4Attr.put("description", "This is the most crowded beach in the planet, lots of trash is being thrown on the ground.");
	    
	    ElementBoundary element6=new ElementBoundary("Banana Beach",empty, "sea_location", false, new Date(), new CreatedBy( 
		 		  new acs.elementBoundaryPackage.UserId(userid1.getDomain(),userid1.getEmail())),
				  new Location(2.1,2.1), el4Attr);
		         
	    
	         ///create all users
	         userService.createUser(user1);
//	         userService.createUser(user2);
	         userService.createUser(user3);
	         
	         
	         
	         //crate all elements
	         ElementBoundary eb1 = elementService.create(user1.getUserId().getDomain(),user1.getUserId().getEmail(), element1);
	         ElementBoundary eb2 = elementService.create(user1.getUserId().getDomain(),user1.getUserId().getEmail(), element2);
	          elementService.create(user1.getUserId().getDomain(),user1.getUserId().getEmail(), element3);
	         elementService.create(user1.getUserId().getDomain(),user1.getUserId().getEmail(), element4);
	         elementService.create(user1.getUserId().getDomain(),user1.getUserId().getEmail(), element5);
	         elementService.create(user1.getUserId().getDomain(),user1.getUserId().getEmail(), element6);
	         
	         
	         
	         //retrive all elemetn
	        // elementService.getAll(user2.getUserId().getDomain(), user2.getUserId().getEmail());
	      // init report 
	 		Report r1 = new Report();
	 		r1.setComment("report demo1");
	 		r1.setTrashLevel(4);
	 		HashMap<String,Object> map1 = new HashMap<String, Object>();
	 		map1.put("report", r1);

	 		ActionBoundary newActiontPosted1 = new ActionBoundary();
	 		newActiontPosted1.setType("addReport");
	 		newActiontPosted1.setInvokedBy(new InvokedBy(new UserId(user3.getUserId().getDomain(), 
	 				user3.getUserId().getEmail())));
	 		newActiontPosted1.setElement(new Element(
	 				new ElementId(eb1.getElementId().getDomain(),
	 						eb1.getElementId().getId())));
	 		newActiontPosted1.setActionAttributes(map1);
	 		
	 		// create action
	 		actionService.invokeAction(newActiontPosted1);
	 		//retrive all elemetn
	 		// elementService.getAll(user2.getUserId().getDomain(), user2.getUserId().getEmail());

	 		
	 		Report r3 = new Report();
	 		r3.setComment("report demo3");
	 		r3.setTrashLevel(2);
	 		HashMap<String,Object> map3 = new HashMap<String, Object>();
	 		map3.put("report", r3);

	 		ActionBoundary newActiontPosted3 = new ActionBoundary();
	 		newActiontPosted3.setType("addReport");
	 		newActiontPosted3.setInvokedBy(new InvokedBy(new UserId(user3.getUserId().getDomain(), 
	 				user3.getUserId().getEmail())));
	 		newActiontPosted3.setElement(new Element(
	 				new ElementId(eb2.getElementId().getDomain(),
	 						eb2.getElementId().getId())));
	 		newActiontPosted3.setActionAttributes(map3);
	 		
	 		// create action
	 		actionService.invokeAction(newActiontPosted3);
	          
	        	
	}
	
}