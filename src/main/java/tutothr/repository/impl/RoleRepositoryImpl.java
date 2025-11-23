package tutothr.repository.impl;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tutothr.model.Role;
import tutothr.repository.RoleRepositoryI;

@Repository
public interface RoleRepositoryImpl extends RoleRepositoryI, CrudRepository<Role, Long> {

}
