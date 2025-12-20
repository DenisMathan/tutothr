package tutothr.user;

import java.net.Authenticator;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import tutothr.auth.CustomOidcUser;
import tutothr.user.interfaces.UserRepositoryI;
import tutothr.user.interfaces.UserServiceI;

@Service
public class UserService implements UserServiceI {

	UserRepositoryI userRepository;

	public UserService(UserRepositoryI userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public Page<User> getAllUsers(String name, Pageable pageable) {
		Page<User> pageUsers;
		if (name == null) {
			pageUsers = userRepository.findAll(pageable);
		} else {
			pageUsers = userRepository.findByUsernameContainingIgnoreCase(name, pageable);

		}
		return pageUsers;
	}

	@Override
	public User saveUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public User getUserById(Long id) {
		return userRepository.findById(id).get();
	}

	@Override
	public User updateUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public void delete(User user) {
		userRepository.delete(user);
	}

	public void updateUsername(User user, Authentication currentAuth) {
		// 1. DB Update
		User _user = userRepository.findById(user.getId()).orElseThrow();
		_user.setUsername(user.getUsername());
		userRepository.save(_user);

		// 2. Security Context Update
		if (currentAuth.getPrincipal() instanceof CustomOidcUser) {
			CustomOidcUser oldPrincipal = (CustomOidcUser) currentAuth.getPrincipal();
			CustomOidcUser newPrincipal = new CustomOidcUser(oldPrincipal, _user);
			OAuth2AuthenticationToken oldToken = (OAuth2AuthenticationToken) currentAuth;
			OAuth2AuthenticationToken newToken = new OAuth2AuthenticationToken(newPrincipal, oldToken.getAuthorities(), oldToken.getAuthorizedClientRegistrationId());
			SecurityContextHolder.getContext().setAuthentication(newToken);
		}
	}
}
