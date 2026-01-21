package tutothr.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tutothr.auth.config.CustomOidcUser;
import tutothr.auth.verifikation.VerificationRepositoryI;
import tutothr.common.BaseService;
import tutothr.course.Course;
import tutothr.course.interfaces.CourseRepositoryI;
import tutothr.hashtag.HashtagService;
import tutothr.user.interfaces.UserMapperI;
import tutothr.user.interfaces.UserRepositoryI;
import tutothr.user.interfaces.UserServiceI;

@Service
public class UserService extends BaseService<UserDTO, User> implements UserServiceI {

	private final UserRepositoryI userRepository;
	private final HashtagService hashtagService;
	private final VerificationRepositoryI verificationRepository;
	private final CourseRepositoryI courseRepository;

	@Autowired
	private UserMapperI userMapper;

	public UserService(UserRepositoryI userRepository, HashtagService hashtagService, 
			VerificationRepositoryI verificationRepository, CourseRepositoryI courseRepository) {
		super(userRepository);
		this.userRepository = userRepository;
		this.hashtagService = hashtagService;
		this.verificationRepository = verificationRepository;
		this.courseRepository = courseRepository;
	}

	@Override
	public UserDTO update(UserDTO dto) {
		User user = getUserById(dto.getId());
		userMapper.updateUserFromDTO(dto, user);
		userRepository.save(user);
		return mapToDTO(user);
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
	public Page<UserDTO> getAllUsersDTO(String name, Pageable pageable) {
		Page<User> pageUsers = getAllUsers(name, pageable);
		return pageUsers.map(userMapper::toUserDTO);
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
	@Transactional
	public void delete(User user) {
		// 1. Verifications löschen
		verificationRepository.findByUserId(user.getId()).ifPresent(verificationRepository::delete);
		
		// 2. Hashtags lösen
		hashtagService.releaseHashtagsFromCreator(user);
		
		// 3. Kurse des Users löschen (damit FK Constraint nicht verletzt wird)
		List<Course> courses = courseRepository.findByOwner(user);
		for (Course course : courses) {
			courseRepository.delete(course);
		}
		
		// 4. User löschen
		userRepository.delete(user);
	}

	public UserDTO mapToDTO(User user) {
		return userMapper.toUserDTO(user);
	}
	public User mapToEntity(UserDTO dto) {
		return userMapper.toEntity(dto);
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

	@Transactional
	public boolean incrementStrikes(Long userId) {
		User user = getUserById(userId);

		int currentStrikes = user.getStrikes();
		int newStrikes = currentStrikes + 1;

		user.setStrikes(newStrikes);

		// Bei 3 Strikes: Account automatisch sperren
		if (newStrikes >= 3) {
			user.setAccountNonLocked(false);
			userRepository.save(user);

			System.out.println("⚠️ USER GESPERRT: " + user.getUsername() +
					" (ID: " + user.getId() + ") nach " + newStrikes + " Strikes");

			return true;  // User wurde durch diesen Strike gesperrt
		}

		userRepository.save(user);

		System.out.println("⚠️ Strike vergeben an " + user.getUsername() +
				" (Strikes: " + newStrikes + "/3)");

		return false;  // User noch nicht gesperrt
	}

	@Transactional
	public void banUser(Long userId) {
		User user = getUserById(userId);

		if (!user.isAccountNonLocked()) {
			throw new IllegalStateException("User ist bereits gesperrt");
		}

		user.setAccountNonLocked(false);
		userRepository.save(user);

		System.out.println("🔒 USER MANUELL GESPERRT: " + user.getUsername() +
				" (ID: " + user.getId() + ")");
	}

	@Transactional
	public void unbanUser(Long userId) {
		User user = getUserById(userId);

		if (user.isAccountNonLocked()) {
			throw new IllegalStateException("User ist nicht gesperrt");
		}

		user.setAccountNonLocked(true);
		user.setStrikes(0);  // Strikes zurücksetzen bei Entsperrung
		userRepository.save(user);

		System.out.println("🔓 USER ENTSPERRT: " + user.getUsername() +
				" (ID: " + user.getId() + ") - Strikes zurückgesetzt");
	}

	@Transactional
	public void resetStrikes(Long userId) {
		User user = getUserById(userId);

		int oldStrikes = user.getStrikes();
		user.setStrikes(0);
		userRepository.save(user);

		System.out.println("↺ Strikes zurückgesetzt für " + user.getUsername() +
				" (von " + oldStrikes + " auf 0)");
	}

	public boolean isUserBanned(Long userId) {
		User user = getUserById(userId);
		return !user.isAccountNonLocked();
	}

	public List<User> getBannedUsers() {
		return userRepository.findByAccountNonLockedFalse();
	}

	public List<User> getUsersWithStrikes() {
		return userRepository.findByStrikesGreaterThan(0);
	}
}
