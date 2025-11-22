package tutothr.repository.impl;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tutothr.model.User;
import tutothr.repository.UserRepositoryI;

@Repository
public interface UserRepositoryImpl extends UserRepositoryI, CrudRepository<User, Long>{
}
