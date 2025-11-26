package tutothr.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ErrorController {

    @GetMapping({"/403", "/404"})
    public String accessDenied() {
        return "404";
    }
    
}
