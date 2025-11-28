package tutothr.role;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tutothr.common.MyBaseRepository;

public interface RoleRepositoryI extends MyBaseRepository<Role, Long>, JpaRepository<Role, Long>  {
	Optional<Role> findByDescriptionIgnoreCase(String description);
}
