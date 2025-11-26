package tutothr.user.implementations;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import tutothr.common.config.MyUserDetails;
import tutothr.user.User;
import tutothr.user.interfaces.UserRepositoryI;
import tutothr.user.interfaces.UserServiceI;

@Service
public class UserServiceImpl implements UserServiceI{

	UserRepositoryI userRepository;

	public UserServiceImpl (UserRepositoryI userRepository) {
		this.userRepository= userRepository;
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

	// @Override
	// public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	// 	System.out.println("I'm here");
	// 	Optional<User> oUser= userRepository.findByEmailIgnoreCase(email);
	// 	oUser.orElseThrow(()-> new UsernameNotFoundException("Not found "+email));
	// 	System.out.println("User found at the UserDetailsService="+ oUser.get().getUsername());
	// 	return new MyUserDetails(oUser.get());
	// }
}
