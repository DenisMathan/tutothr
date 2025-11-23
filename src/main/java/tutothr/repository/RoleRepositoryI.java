package tutothr.repository;

import java.util.Optional;

import tutothr.model.Role;

public interface RoleRepositoryI extends MyBaseRepository<Role, Long> {
	Optional<Role> findByDescriptionIgnoreCase(String description);
}
