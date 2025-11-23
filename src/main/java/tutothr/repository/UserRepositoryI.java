package tutothr.repository;
import java.util.Optional;
import tutothr.model.User;

public interface UserRepositoryI extends MyBaseRepository<User, Long> {
	Optional<User> findByLoginIgnoreCase(String login);
}
