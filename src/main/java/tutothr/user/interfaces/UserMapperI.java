package tutothr.user.interfaces;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.AfterMapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import tutothr.user.User;
import tutothr.user.UserDTO;
import tutothr.auth.dtos.RegisterUserDTO;
import tutothr.role.Role;
import tutothr.role.RoleRepositoryI;
import tutothr.common.utils.enums.RolesEnum;

import java.util.HashSet;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class UserMapperI {

    @Autowired
    protected RoleRepositoryI roleRepository;

    public abstract RegisterUserDTO toDTO(User user);
    public abstract User toEntity(RegisterUserDTO dto);

    public abstract UserDTO toUserDTO(User user);
    public abstract User toEntity(UserDTO dto);

    @AfterMapping
    protected void mapRolesToBooleans(User user, @MappingTarget UserDTO dto) {
        if (user.getRoles() == null) return;
        dto.setAdmin(user.getRoles().stream().anyMatch(role -> role.getType() == RolesEnum.ADMIN));
        dto.setTutor(user.getRoles().stream().anyMatch(role -> role.getType() == RolesEnum.TUTOR));
        dto.setStudent(user.getRoles().stream().anyMatch(role -> role.getType() == RolesEnum.STUDENT));
    }

    @AfterMapping
    protected void mapBooleansToRoles(UserDTO dto, @MappingTarget User user) {
        Set<Role> roles = new HashSet<>();
        if (dto.isAdmin()) {
            roleRepository.findByType(RolesEnum.ADMIN).ifPresent(roles::add);
        }
        if (dto.isTutor()) {
            roleRepository.findByType(RolesEnum.TUTOR).ifPresent(roles::add);
        }
        if (dto.isStudent()) {
            roleRepository.findByType(RolesEnum.STUDENT).ifPresent(roles::add);
        }
        user.setRoles(roles);
    }
}
