package acs.data;

import acs.usersBoundaryPackage.UserBoundary;

import org.springframework.stereotype.Component;

import acs.usersBoundaryPackage.User;
@Component
public class UserEntityBoundaryConvertor {

	public UserBoundary entityToBoundary(UserEntity entity) {
		UserBoundary newUserBoundary = new UserBoundary();
		newUserBoundary.setAvatar(entity.getAvatar());
		newUserBoundary.setUsername(entity.getUsername());
		newUserBoundary.setUserId(new User(entity.getUserId().getDomain(), entity.getUserId().getEmail()));
		newUserBoundary.setDetails(entity.getDetails());
		newUserBoundary.setDeleted(entity.getDeleted());
		newUserBoundary.setTimestamp(entity.getTimestemp());

		if (entity.getRole() != null) {
			newUserBoundary.setRole(entityToBoundaryRole(entity.getRole()));
		}

		return newUserBoundary;
	}

	public UserEntity boundarytoEntity(UserBoundary boundary) {
		UserEntity newUserEntity = new UserEntity();
		newUserEntity.setAvatar(boundary.getAvatar());
		newUserEntity.setUsername(boundary.getUsername());
		newUserEntity.setUserId(new acs.data.userEntityProperties.User(boundary.getUserId().getDomain(),
				boundary.getUserId().getEmail()));

		if (boundary.getDeleted() != null) {
			newUserEntity.setDeleted(boundary.getDeleted());
		} else {
			newUserEntity.setDeleted(false);
		}

		if (boundary.getRole() != null) {
			newUserEntity.setRole(boundaryToEntityRole(boundary.getRole()));
		}
		return newUserEntity;
	}

	public acs.data.userEntityProperties.Roles boundaryToEntityRole(String role) {
		return acs.data.userEntityProperties.Roles.valueOf(role);
	}

	public String entityToBoundaryRole(acs.data.userEntityProperties.Roles role) {
		return role.toString();

	}

}
