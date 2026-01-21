package tutothr.user.interfaces;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.AfterMapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import tutothr.user.User;
import tutothr.user.UserDTO;
import tutothr.auth.dtos.RegisterUserDTO;
import tutothr.common.utils.enums.RolesEnum;

import java.util.HashSet;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class UserMapperI {


    public abstract RegisterUserDTO toDTO(User user);
    public abstract User toEntity(RegisterUserDTO dto);

    public abstract UserDTO toUserDTO(User user);
    public abstract User toEntity(UserDTO dto);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "authProvider", ignore = true)
    @Mapping(target = "username", source = "username")
    public abstract void updateUserFromDTO(UserDTO dto, @MappingTarget User user);

    @AfterMapping
    protected void mapRolesToBooleans(User user, @MappingTarget UserDTO dto) {
        if (user.getRoles() == null) return;
        dto.setAdmin(user.getRoles().contains(RolesEnum.ADMIN));
        dto.setTutor(user.getRoles().contains(RolesEnum.TUTOR));
        dto.setStudent(user.getRoles().contains(RolesEnum.STUDENT));
    }

    @AfterMapping
    protected void mapBooleansToRoles(UserDTO dto, @MappingTarget User user) {
        // If all role flags are null, do not update roles (assume no role fields in form)
        if (dto.getAdmin() == null && dto.getTutor() == null && dto.getStudent() == null) {
            return;
        }

        Set<RolesEnum> roles = new HashSet<>();
        
        // Use Boolean.TRUE.equals to safely handle nulls
        if (Boolean.TRUE.equals(dto.getAdmin())) {
            roles.add(RolesEnum.ADMIN);
        }
        if (Boolean.TRUE.equals(dto.getTutor())) {
            roles.add(RolesEnum.TUTOR);
        }
        if (Boolean.TRUE.equals(dto.getStudent())) {
            roles.add(RolesEnum.STUDENT);
        }
        user.setRoles(roles);
    }
}
