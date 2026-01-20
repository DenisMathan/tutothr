package tutothr.user.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import tutothr.user.User;
import tutothr.user.UserDTO;

public interface UserServiceI {
    Page<User> getAllUsers(String name, Pageable pageable);
    
    Page<UserDTO> getAllUsersDTO(String name, Pageable pageable);

    User saveUser(User user);

    User getUserById(Long id);

    User updateUser(User user);

    void delete(User user);

}
