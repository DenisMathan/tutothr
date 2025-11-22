package tutothr.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import tutothr.model.User;
import tutothr.repository.UserRepositoryI;

@Controller
public class UserController {
    private UserRepositoryI userRepository;

    public UserController(UserRepositoryI userRepository) {
        super();
        System.out.println("controller is here");
        this.userRepository = userRepository;
    }

    @GetMapping(value = {"", "/all" })
    public String showUserList( Model model, 
                                @RequestParam(name = "keyword", required = false) String keyword, 
                                @RequestParam(name = "page", required = false, defaultValue = "1") int page, 
                                @RequestParam(name = "size", required = false, defaultValue = "5") int size) {
        System.out.println("where is list?");
        try {
            
            // simple path: list all users instead of paginated students
            Iterable<User> usersIter = userRepository.findAll();
            List<User> users = new ArrayList<>();
            usersIter.forEach(users::add);

            model.addAttribute("keyword", keyword);
            model.addAttribute("users", users);
            // no pagination in this simple view
            model.addAttribute("entitytype", "user");
            model.addAttribute("totalItems", users.size());
            model.addAttribute("pageSize", users.size());
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        return "users/user-all";
    }
}
