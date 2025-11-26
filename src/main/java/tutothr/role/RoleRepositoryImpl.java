package tutothr.role;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepositoryImpl extends RoleRepositoryI, CrudRepository<Role, Long> {

}
