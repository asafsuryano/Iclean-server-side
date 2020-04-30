package acs.data.dal;

import org.springframework.data.repository.CrudRepository;

import acs.data.UserEntity;
import acs.data.elementEntityProperties.UserId;
import acs.data.userEntityProperties.User;


public interface UserDao extends CrudRepository<UserEntity, User> {}
