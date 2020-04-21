package acs.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import acs.logic.UserService;
import acs.newUserDetailsBoundaryPackage.NewUserDetails;
import acs.usersBoundaryPackage.User;
import acs.usersBoundaryPackage.UserBoundary;

@RestController
public class UserController {
	UserService userService;
	
	@Autowired
	public UserController() {
	}
	
	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}


	@RequestMapping(path="/acs/users",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary createNewUser(@RequestBody  NewUserDetails user) {
		// need to return UserBoundary with the properties of the input from NewUserDetails
		UserBoundary ub = new UserBoundary();
		ub.setUsername(user.getUsername());
		ub.setAvatar(user.getAvatar());
		ub.setRole(user.getRole());
		ub.setUserId(new User("",user.getEmail()));
		
		return this.userService.createUser(ub);
	}

	
	@RequestMapping(path="/acs/users/{userDomain}/{userEmail}",
			method = RequestMethod.PUT,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateUser(@RequestBody  UserBoundary user, 
									@PathVariable("userDomain") String domain,
									@PathVariable("userEmail") String email) {
		this.userService.updateUser(domain, email, user);
	}
	
	@RequestMapping(path="/acs/users/login/{userDomain}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary login(@PathVariable("userDomain") String domain,
									@PathVariable("userEmail") String email) {
		return this.userService.login(domain, email);
	}
		
}
