package tutothr.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import tutothr.model.Student;
import tutothr.model.User;

public interface UserRepositoryI extends MyBaseRepository<User, Long> {
	Optional<User> findByLoginIgnoreCase(String login);
}
