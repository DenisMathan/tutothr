package tutothr.role;

import java.util.Optional;

import tutothr.common.MyBaseRepository;
import tutothr.common.utils.enums.RolesEnum;

public interface RoleRepositoryI extends MyBaseRepository<Role, Long> {
	Optional<Role> findByDescriptionIgnoreCase(String description);
	Optional<Role> findByType(RolesEnum type);
}
