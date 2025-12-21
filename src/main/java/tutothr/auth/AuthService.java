package tutothr.auth;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import tutothr.auth.config.MyUserDetails;
import tutothr.auth.dtos.RegisterUserDTO;
import tutothr.common.BaseService;
import tutothr.common.services.MailService;
import tutothr.role.RoleRepositoryI;
import tutothr.user.User;
import tutothr.user.interfaces.UserMapperI;
import tutothr.user.interfaces.UserRepositoryI;

@Service
public class AuthService extends BaseService<RegisterUserDTO, User> implements UserDetailsService {

    private final RoleRepositoryI roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final UserMapperI userMapper;

    public AuthService(UserRepositoryI userRepository, RoleRepositoryI roleRepository, PasswordEncoder passwordEncoder, MailService mailService, UserMapperI userMapper) {
        super(userRepository);
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.userMapper = userMapper;
    }

    public boolean confirmPasswords(RegisterUserDTO form) {
        return form.getPassword() != null && form.getPassword().equals(form.getConfirmPassword());
    }
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> oUser= ((UserRepositoryI) repository).findByEmailIgnoreCase(email);
		User user = oUser.orElseThrow(()-> new UsernameNotFoundException("Not found "+email));
        
        if (user.getAuthProvider() == AuthProvider.GOOGLE) {
            throw new UsernameNotFoundException("Please use Google Login for this account.");
        }

		return new MyUserDetails(user);
	}

    public boolean register(RegisterUserDTO form){

        // username unique prüfen
        if (((UserRepositoryI) repository).findByEmailIgnoreCase(form.getEmail()).isPresent()) {
            form.addValidationError("email", "Eine Registrierung mit dieser E-Mail-Adresse ist bereits vorhanden.");
            return false; // oder bessere Fehlermeldung
        }

        // Benutzer anlegen

        User u = mapToEntity(form);
        u.setActive(true);
        u.setAuthProvider(AuthProvider.LOCAL);
        u.setPassword(passwordEncoder.encode(form.getPassword()));


        // Rolle holen und zuweisen (z.B. STUDENT)
        roleRepository.findByDescriptionIgnoreCase("STUDENT").ifPresent(r -> {
            u.getRoles().add(r);
        });
        repository.save(u);
        try {
            mailService.sendRegistrationMail(form.getEmail(), "Welcome to Tutothr", u.getUsername());
        } catch (Exception e) {
            System.err.println("Failed to send registration email: " + e.getMessage());
        }
        return true;
    }

    public void login(){}

    @Override
    public RegisterUserDTO mapToDTO(User entity) {
        return userMapper.toDTO(entity);
    }

    @Override
    public User mapToEntity(RegisterUserDTO dto) {
        return userMapper.toEntity(dto);
    }
    
}
