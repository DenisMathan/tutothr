package tutothr.role;
import java.util.Set;
import tutothr.common.utils.enums.PermissionsEnum;
import tutothr.common.utils.enums.RolesEnum;
import java.util.EnumSet; // oben in der Datei


public class AdminRole implements RoleI{
	Set<PermissionsEnum> permissions;
	public AdminRole() {
		permissions = EnumSet.of(PermissionsEnum.TEST1);
	}

	@Override
	public RolesEnum getRoleType() {
		return RolesEnum.ADMIN;
	}

	@Override
	public Set<PermissionsEnum> getPermissions() {
		return this.permissions;
	}

	//TODO Add Methods
	public void bookCourse() {};
	public void cancelBooking() {};
	public void createRating() {};
}