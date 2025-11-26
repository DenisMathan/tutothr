package tutothr.repository;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import tutothr.model.User;

public interface UserRepositoryI extends MyBaseRepository<User, Long> {
	// Optional<User> findByUsernameIgnoreCase(String username);
	Optional<User> findByEmailIgnoreCase(String email);
	Page<User> findAll(Pageable pageable);

    Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
}
