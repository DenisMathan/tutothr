package tutothr.auth;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import tutothr.common.config.MyUserDetails;
import tutothr.role.RoleRepositoryI;
import tutothr.user.User;
import tutothr.user.interfaces.UserRepositoryI;

@Service
public class AuthService implements UserDetailsService {

    private final UserRepositoryI userRepository;
    private final RoleRepositoryI roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepositoryI userRepository, RoleRepositoryI roleRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> oUser= userRepository.findByEmailIgnoreCase(email);
		oUser.orElseThrow(()-> new UsernameNotFoundException("Not found "+email));
		System.out.println("User found at the UserDetailsService="+ oUser.get().getEmail());
		return new MyUserDetails(oUser.get());
	}

    public boolean register(RegisterUserDTO form){

        if (form.getUsername() == null || form.getUsername().isBlank()) {
            return false;
        }

        if (form.getPassword() == null || !form.getPassword().equals(form.getConfirmPassword())) {
            // best practise: add FieldError and return view so errors show in form
            return false;
        }
        // username unique prüfen
        if (userRepository.findByEmailIgnoreCase(form.getEmail()).isPresent()) {
            return false; // oder bessere Fehlermeldung
        }

        // Benutzer anlegen
        tutothr.user.User u = new tutothr.user.User();
        u.setUsername(form.getUsername());
        u.setEmail(form.getEmail());
        u.setPassword(passwordEncoder.encode(form.getPassword()));
        u.setActive(true);

        // Rolle holen und zuweisen (z.B. STUDENT)
        roleRepository.findByDescriptionIgnoreCase("STUDENT").ifPresent(r -> {
            u.getRoles().add(r);
        });
        userRepository.save(u);
        return true;
    }
    public void login(){}
    
}
