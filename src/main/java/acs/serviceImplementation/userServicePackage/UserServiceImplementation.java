package acs.serviceImplementation.userServicePackage;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import acs.actionBoundaryPackage.UserId;
import acs.data.UserEntity;
import acs.data.UserEntityBoundaryConvertor;
import acs.data.userEntityProperties.User;
import acs.logic.UserService;
import acs.newUserDetailsBoundaryPackage.NewUserDetails;
import acs.usersBoundaryPackage.Roles;
import acs.usersBoundaryPackage.UserBoundary;

public class UserServiceImplementation implements UserService {
	private UserEntityBoundaryConvertor converter;
	private String projectName;
	// private Map<String, UserEntity> userDatabase;
	private Map<User, UserEntity> userDatabase;

	// injection of value from the spring boot configuration
	@Value("${spring.application.name:demo}")
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Autowired
	public UserServiceImplementation(UserEntityBoundaryConvertor convertor) {
		this.converter = convertor;
	}

	@PostConstruct
	public void init() {
		// since this class is a singleton, we generate a thread safe collection
		this.userDatabase = Collections.synchronizedMap(new TreeMap<>());
	}

	@Override
	public UserBoundary createUser(UserBoundary user) {
		user.getUserId().setDomain(this.projectName);
		if (user.getRole() == null) {
			user.setRole(Roles.USER);
		}
		if (user.getDetails() == null) {
			user.setDetails(new HashMap<>());
		}
		user.setDeleted(false);
		user.setTimestamp(new Date());
		UserEntity newUserEntity = this.converter.boundarytoEntity(user);
		UserEntity exiting = this.userDatabase.get(newUserEntity.getUserId());

		if (exiting != null) {
			if (exiting.getDeleted())
				exiting.setDeleted(false);// old user recreate account
			else
				throw new UserAllReadyExsistExeption("User allready exsist ");// try to hack the system
		} else
			this.userDatabase.put(newUserEntity.getUserId(), newUserEntity);// new user in system

		return user;
	}

	@Override
	public UserBoundary login(String userDomain, String userEmail) {
		User exsistUser = new User(userDomain, userEmail);
		UserEntity getUser = this.userDatabase.get(exsistUser);
		if (getUser != null && getUser.getDeleted() == false) {
			return this.converter.entityToBoundary(getUser);
		} else {
			throw new UserNotFoundException("User Not Found With This UserDomain and Email");
		}
	}

	@Override
	public UserBoundary updateUser(String userDomain, String userEmail, UserBoundary update) {
		UserBoundary existingUser = new UserBoundary();
		existingUser.setDeleted(update.getDeleted());

		if (update.getUserId() != null) {
			existingUser.getUserId().setDomain(userDomain);
			existingUser.getUserId().setEmail(userEmail);
		}

		if (update.getRole() != null) {
			existingUser.setRole(update.getRole());

		}

		if (update.getUsername() != null) {
			existingUser.setUsername(update.getUsername());

		}

		if (update.getAvatar() != null) {
			existingUser.setAvatar(update.getAvatar());

		}

		if (update.getDetails() != null) {
			existingUser.setDetails(update.getDetails());

		}

		if (update.getTimestamp() != null) {
			existingUser.setTimestamp(update.getTimestamp());

		}

		return existingUser;
	}

	@Override
	public List<UserBoundary> getAllUsers(String adminDomain, String adminEmail) {
	
		return null;
	}

	@Override
	public void deleteAllUsers(String adminDomain, String adminEmail) {
		// TODO Auto-generated method stub

	}

}
