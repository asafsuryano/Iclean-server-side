package acs.serviceImplementation.userServicePackage;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import acs.Utils.StringUtil;
import acs.data.UserEntity;
import acs.data.UserEntityBoundaryConvertor;
import acs.data.UserRoles;
import acs.data.userEntityProperties.User;
import acs.logic.UserService;
import acs.usersBoundaryPackage.UserBoundary;

@Service
public class UserServiceImplementation implements UserService {
	private UserEntityBoundaryConvertor converter;
	private String projectName;
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
		this.userDatabase = Collections.synchronizedMap(new HashMap<>());
	}

	@Override
	public UserBoundary createUser(UserBoundary user) {
		if(StringUtil.isNullOrEmpty(user.getUserId().getEmail()))
			throw new RuntimeException("email invalid");
		else if (StringUtil.isNullOrEmpty(user.getUsername()))
			throw new RuntimeException("username invalid");
		user.getUserId().setDomain(this.projectName);
		if (user.getRole() == null) {
			user.setRole(UserRoles.PLAYER.toString());
		}
		UserEntity newUserEntity = this.converter.boundaryToEntity(user);
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
		// THE UPDATER OF THE USER
		User updater = new User(userDomain, userEmail);
		UserEntity updaterUser = this.userDatabase.get(updater);// this is the manager!!

		if (updaterUser == null || updaterUser.getDeleted()) {
			throw new UserNotFoundException("Update Fail, the updater details is incorrect");
		}

		if (updaterUser.getRole() != UserRoles.ADMIN ||updaterUser.getRole() != UserRoles.MANAGER  ) {
			//updater not MANAGER or ADMIN
			if ((!userDomain.equals(update.getUserId().getDomain())) || (!userEmail.equals(update.getUserId().getEmail())))
				//updater is not update
				throw new NoPermissionsExeption("This user does not have permission");
		}
		// THE USER TO UPDATE
		UserEntity existingUser = this.userDatabase.get(this.converter.boundaryToEntity(update).getUserId());

		if (existingUser == null || existingUser.getDeleted()) {
			throw new UserNotFoundException("Update Fail, User to update is not exist");
		}

		if (update.getRole() != null) {
			existingUser.setRole(this.converter.boundaryToEntityRole(update.getRole()));

		}

		if (update.getUsername() != null) {
			existingUser.setUsername(update.getUsername());
		}

		if (update.getAvatar() != null) {
			existingUser.setAvatar(update.getAvatar());

		}
		this.userDatabase.replace(existingUser.getUserId(), existingUser);
		return this.converter.entityToBoundary(existingUser);
	}

	@Override
	public List<UserBoundary> getAllUsers(String adminDomain, String adminEmail) {
		User adminUser = new User(adminDomain, adminEmail);
		UserEntity admin = this.userDatabase.get(adminUser);

		if (admin != null) {//if the user that create the request is exist
			if (admin.getRole() == acs.data.UserRoles.ADMIN) {//is user have right permissions 
				return this.userDatabase.values().stream().map(this.converter::entityToBoundary)
						.collect(Collectors.toList());
			}
			else throw new NoPermissionsExeption("This user is not a admin");
		}
		throw new UserNotFoundException("user is not exist in the system");
	}

	@Override
	public void deleteAllUsers(String adminDomain, String adminEmail) {
		User admidUser = new User(adminDomain, adminEmail);
		UserEntity admin = this.userDatabase.get(admidUser);

		if (admin != null) {
			if (admin.getRole() == acs.data.UserRoles.ADMIN) {
				this.userDatabase.clear();
			}
			else throw new NoPermissionsExeption("This user is not a admin");
		}
		else
			throw new UserNotFoundException("user is not exist in the system");
	}

}
