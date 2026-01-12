package tutothr.role;
import java.util.Set;
import tutothr.common.utils.enums.PermissionsEnum;
import tutothr.common.utils.enums.RolesEnum;
import java.util.EnumSet; // oben in der Datei


public class TutorRole implements RoleI {
	Set<PermissionsEnum> permissions;
	public TutorRole() {
		permissions = EnumSet.of(PermissionsEnum.TEST1);
	}

	@Override
	public RolesEnum getRoleType() {
		return RolesEnum.TUTOR;
	}

	@Override
	public Set<PermissionsEnum> getPermissions() {
		return this.permissions;
	}
	//TODO METHODS
}