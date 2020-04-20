package acs.serviceImplementation.userServicePackage;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import acs.data.UserConvertor;
import acs.data.UserEntity;
import acs.logic.UserService;
import acs.newUserDetailsBoundaryPackage.NewUserDetails;
import acs.usersBoundaryPackage.Roles;
import acs.usersBoundaryPackage.UserBoundary;

public class UserServiceImplementation implements UserService {
UserConvertor converter =new UserConvertor();
private Map<String, UserEntity> userDatabase;
	@Override
	public UserBoundary createUser(UserBoundary user) {
          if (user.getRole()==null) {
        	  user.setRole(Roles.USER);
          }
          if (user.getDetails()==null) {
        	  user.setDetails(new HashMap<>());
          }
          user.setDeleted(false);
          UserEntity newUserEntity=this.converter.toEntity(user);
          user.setTimestamp(new Date());
          
         this.userDatabase.put(user.getUserId().getDomain()+"#"+user.getUserId().getEmail(),newUserEntity);
         return this.converter.fromEntity(newUserEntity);
	}

	@Override
	public UserBoundary login(String userDomain, String userEmail) {
	UserEntity getUser=this.userDatabase.get(userDomain+"#"+userEmail);
		if (getUser!=null) {
			return this.converter.fromEntity(getUser);
		}
		else {
			throw new UserNotFoundException("User Not Found With This UserDomain and Email");
		}
	}

	@Override
	public UserBoundary updateUser(String userDomain, String userEmail, UserBoundary update) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserBoundary> getAllUsers(String adminDomain, String adminEmail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAllUsers(String adminDomain, String adminEmail) {
		// TODO Auto-generated method stub
		
	}

}
