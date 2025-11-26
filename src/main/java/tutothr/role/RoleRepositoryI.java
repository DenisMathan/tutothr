package tutothr.role;

import java.util.Optional;

import tutothr.common.MyBaseRepository;

public interface RoleRepositoryI extends MyBaseRepository<Role, Long> {
	Optional<Role> findByDescriptionIgnoreCase(String description);
}
