package tutothr.role;

import java.util.Set;

import tutothr.common.utils.enums.PermissionsEnum;
import tutothr.common.utils.enums.RolesEnum;

public interface RoleI {
    public RolesEnum getRoleType();
    public Set<PermissionsEnum> getPermissions();
} 
