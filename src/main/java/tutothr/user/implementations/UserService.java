package tutothr.user.implementations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tutothr.user.User;
import tutothr.user.interfaces.UserRepositoryI;
import tutothr.user.interfaces.UserServiceI;

@Service
public class UserService implements UserServiceI{

	UserRepositoryI userRepository;

	public UserService (UserRepositoryI userRepository) {
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
}
