package tutothr.user.implementations;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tutothr.user.User;
import tutothr.user.interfaces.UserRepositoryI;

@Repository
public interface UserRepositoryImpl extends UserRepositoryI, JpaRepository<User, Long> {
}
