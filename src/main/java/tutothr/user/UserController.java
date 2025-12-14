package tutothr.user;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import tutothr.user.interfaces.UserRepositoryI;

@Controller
public class UserController {
    private UserRepositoryI userRepository;

    public UserController(UserRepositoryI userRepository) {
        super();
        this.userRepository = userRepository;
    }

    @GetMapping(value = {"", "/admin/all" })
    public String showUserList( Model model, 
                                @RequestParam(required = false) String keyword, 
                                @RequestParam(required = false, defaultValue = "1") int page, 
                                @RequestParam(required = false, defaultValue = "5") int size) {
        try {
            
            // simple path: list all users instead of paginated students
            List<User> users = userRepository.findAll();
            model.addAttribute("keyword", keyword);
            model.addAttribute("users", users);
            // no pagination in this simple view
            model.addAttribute("entitytype", "user");
            model.addAttribute("totalItems", users.size());
            model.addAttribute("pageSize", users.size());
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        return "/views/users/user-all";
    }
}
