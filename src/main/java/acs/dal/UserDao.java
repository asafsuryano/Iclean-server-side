package acs.dal;

import org.springframework.data.repository.PagingAndSortingRepository;

import acs.data.UserEntity;
import acs.data.userEntityProperties.User;


public interface UserDao extends PagingAndSortingRepository<UserEntity, User> {}
