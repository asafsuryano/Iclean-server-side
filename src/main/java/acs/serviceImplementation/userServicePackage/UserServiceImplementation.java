package acs.serviceImplementation.userServicePackage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import acs.Utils.StringUtil;
import acs.data.UserEntity;
import acs.data.UserEntityBoundaryConvertor;
import acs.data.UserRoles;
import acs.data.dal.UserDao;
import acs.data.userEntityProperties.User;
import acs.logic.UserService;
import acs.usersBoundaryPackage.UserBoundary;

@Service
public class UserServiceImplementation implements UserService {
	private UserEntityBoundaryConvertor converter;
	private String projectName;
	private UserDao userDatabase;

	// injection of value from the spring boot configuration
	@Value("${spring.application.name:demo}")
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Autowired
	public UserServiceImplementation(UserEntityBoundaryConvertor convertor, UserDao userDatabase) {
		this.converter = convertor;
		this.userDatabase = userDatabase;
	}

	@PostConstruct
	public void init() {
		// since this class is a singleton, we generate a thread safe collection
		// this.userDatabase = Collections.synchronizedMap(new HashMap<>());
	}

	@Override
	@Transactional
	public UserBoundary createUser(UserBoundary user) {
		if (!StringUtil.isEmailGood(user.getUserId().getEmail()))
			throw new RuntimeException("email invalid");
		
		else if (StringUtil.isNullOrEmpty(user.getUsername()))
			throw new RuntimeException("username invalid");
		
		user.getUserId().setDomain(this.projectName);
		if (user.getRole() == null) {
			user.setRole(UserRoles.PLAYER.toString());
		}
		
		if (StringUtil.isNullOrEmpty(user.getAvatar()))
			throw new RuntimeException("invalid avatar");
		
		 UserEntity newUserEntity = this.converter.boundaryToEntity(user);		
		 Optional<UserEntity> exiting = this.userDatabase.findById(newUserEntity.getUserId());

		if (exiting.isPresent()) {
			if (exiting.get().getDeleted())
				exiting.get().setDeleted(false);// old user recreate account
			else
				throw new UserAllReadyExsistExeption("User allready exsist ");// try to hack the system
		} else
			this.userDatabase.save(newUserEntity);// new user in system

		return user;
	}

	@Override
	public UserBoundary login(String userDomain, String userEmail) {
		User exsistUser = new User(userDomain, userEmail);
		Optional<UserEntity> getUser = this.userDatabase.findById(exsistUser);
		
		if (getUser.isPresent() && getUser.get().getDeleted() == false) {
			return this.converter.entityToBoundary(getUser.get());
		} else {
			throw new UserNotFoundException("User Not Found With This UserDomain and Email");
		}
	}

	@Override
	@Transactional
	public UserBoundary updateUser(String userDomain, String userEmail, UserBoundary update) {
		// THE UPDATER OF THE USER
		User updater = new User(userDomain, userEmail);
		Optional<UserEntity> updaterUser = this.userDatabase.findById(updater);
		if (!update.getUserId().getEmail().isEmpty())
			if (!StringUtil.isEmailGood(update.getUserId().getEmail()))
				throw new RuntimeException("email invalid");
		if (!updaterUser.isPresent() || updaterUser.get().getDeleted()) {
			throw new UserNotFoundException("Update Fail, the updater details is incorrect");
		}

		if (updaterUser.get().getRole() != UserRoles.ADMIN || updaterUser.get().getRole() != UserRoles.MANAGER) {
			// updater not MANAGER or ADMIN
			if ((!userDomain.equals(update.getUserId().getDomain()))
					|| (!userEmail.equals(update.getUserId().getEmail())))
				// updater is not update
				throw new NoPermissionsExeption("This user does not have permission");
		}
		
		
		// THE USER TO UPDATE
		Optional<UserEntity> existingUser = this.userDatabase.findById(this.converter.boundaryToEntity(update).getUserId());

		if (!existingUser.isPresent() || existingUser.get().getDeleted()) {
			throw new UserNotFoundException("Update Fail, User to update is not exist");
		}

		if (!StringUtil.isNullOrEmpty(update.getRole())) {
			existingUser.get().setRole(this.converter.boundaryToEntityRole(update.getRole()));

		}

		if (!StringUtil.isNullOrEmpty(update.getUsername())) {
			existingUser.get().setUsername(update.getUsername());
		}

		if (!StringUtil.isNullOrEmpty(update.getAvatar())) {
			existingUser.get().setAvatar(update.getAvatar());

		}

	    this.userDatabase.save(existingUser.get());
		return this.converter.entityToBoundary(existingUser.get());
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserBoundary> getAllUsers(String adminDomain, String adminEmail) {
		User adminUser = new User(adminDomain, adminEmail);
		Optional<UserEntity> admin = this.userDatabase.findById(adminUser);

		if (admin.isPresent()) {   // if the user that create the request is exist
			if (admin.get().getRole() == acs.data.UserRoles.ADMIN) {
				return StreamSupport.stream(this.userDatabase.findAll().spliterator(),false).
						map(this.converter::entityToBoundary).collect(Collectors.toList());
			} else
				throw new NoPermissionsExeption("This user is not a admin");
		}
		throw new UserNotFoundException("user is not exist in the system");
	}

	
	@Override
	@Transactional
	public void deleteAllUsers(String adminDomain, String adminEmail) {
		User adminUser = new User(adminDomain, adminEmail);
		Optional<UserEntity> admin = this.userDatabase.findById(adminUser);

		if (admin.isPresent()) {
			if (admin.get().getRole() == acs.data.UserRoles.ADMIN) {
				this.userDatabase.deleteAll();
			} else
				throw new NoPermissionsExeption("This user is not a admin");
		} else
			throw new UserNotFoundException("user is not exist in the system");
	}

}
