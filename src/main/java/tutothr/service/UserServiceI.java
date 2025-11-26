package tutothr.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import tutothr.model.User;
import tutothr.model.User;

public interface UserServiceI {
    Page<User> getAllUsers(String name, Pageable pageable);

    User saveUser(User user);

    User getUserById(Long id);

    User updateUser(User user);

    void delete(User user);

}
