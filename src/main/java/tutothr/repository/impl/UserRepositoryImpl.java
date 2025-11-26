package tutothr.repository.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tutothr.model.User;
import tutothr.repository.UserRepositoryI;

@Repository
public interface UserRepositoryImpl extends UserRepositoryI, JpaRepository<User, Long> {
}
