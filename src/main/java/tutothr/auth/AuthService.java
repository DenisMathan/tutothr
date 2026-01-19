package tutothr.auth;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tutothr.auth.config.AppPrincipal;
import tutothr.auth.config.MyUserDetails;
import tutothr.auth.dtos.RegisterUserDTO;
import tutothr.auth.verifikation.VerificationService;
import tutothr.auth.verifikation.VerificationToken;
import tutothr.common.BaseService;
import tutothr.common.services.MailService;
import tutothr.common.utils.enums.RolesEnum;
import tutothr.user.User;
import tutothr.user.interfaces.UserMapperI;
import tutothr.user.interfaces.UserRepositoryI;

@Service
public class AuthService extends BaseService<RegisterUserDTO, User> implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final UserMapperI userMapper;
    private final VerificationService verificationService;

    public AuthService(UserRepositoryI userRepository, PasswordEncoder passwordEncoder, MailService mailService, UserMapperI userMapper, VerificationService verificationService) {
        super(userRepository);
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.userMapper = userMapper;
        this.verificationService = verificationService;
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

    public User register(RegisterUserDTO form){

        // username unique prüfen
        if (((UserRepositoryI) repository).findByEmailIgnoreCase(form.getEmail()).isPresent()) {
            form.addValidationError("email", "Eine Registrierung mit dieser E-Mail-Adresse ist bereits vorhanden.");
            return null; // oder bessere Fehlermeldung
        }

        // Benutzer anlegen
        User user = mapToEntity(form);
        user.setActive(true);
        user.setAuthProvider(AuthProvider.LOCAL);
        user.setPassword(passwordEncoder.encode(form.getPassword()));


        // Rolle holen und zuweisen (z.B. STUDENT)
        user.getRoles().add(RolesEnum.STUDENT);
        User _user = repository.save(user);
        try {
            VerificationToken veri = verificationService.createToken(user);
            mailService.sendVerificationEmail(user, veri.getToken());
        } catch (Exception e) {
            System.err.println("Failed to send registration email: " + e.getMessage());
        }
        return _user;
    }

    public void login(){} 

    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
    }

    @Override
    public RegisterUserDTO mapToDTO(User entity) {
        return userMapper.toDTO(entity);
    }

    @Override
    public User mapToEntity(RegisterUserDTO dto) {
        return userMapper.toEntity(dto);
    }

    public void checkVerificationStatus(User user) {
        if (!user.isVerified()) {
            // throw new IllegalStateException("User email is not verified.");
        }
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() 
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }
        
        Object principal = authentication.getPrincipal();

        if (principal instanceof AppPrincipal) {
            return ((AppPrincipal) principal).getDbUser();
        }
        
        // Fallback for OAuth2User if it doesn't implement AppPrincipal
        if (principal instanceof org.springframework.security.oauth2.core.user.OAuth2User) {
             String email = ((org.springframework.security.oauth2.core.user.OAuth2User) principal).getAttribute("email");
             if (email != null) {
                 return ((UserRepositoryI) repository).findByEmailIgnoreCase(email).orElse(null);
             }
        }

        String email = authentication.getName();
        return ((UserRepositoryI) repository).findByEmailIgnoreCase(email).orElse(null);
    }
    
}
