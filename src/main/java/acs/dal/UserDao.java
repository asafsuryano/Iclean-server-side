package acs.dal;

import org.springframework.data.repository.CrudRepository;

import acs.data.UserEntity;
import acs.data.userEntityProperties.User;


public interface UserDao extends CrudRepository<UserEntity, User> {}
