package tutothr.rating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import tutothr.auth.config.AppPrincipal;
import tutothr.course.Course;
import tutothr.course.CourseService;
import tutothr.user.User;
import tutothr.user.UserService;

@Controller
public class RatingController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private UserService userService;

    @GetMapping("/courses/{id}/addRating")
    public String showAddRatingForm(@PathVariable Long id, Model model) {
        Course course = courseService.findById(id);

        if (course == null) {
            return "redirect:/courses";
        }

        model.addAttribute("course", course);
        model.addAttribute("rating", new Rating());

        return "views/courses/course-add-rating";
    }

    @PostMapping("/courses/{id}/addRating")
    public String addRating(
            @PathVariable Long id,
            @Valid @ModelAttribute("rating") RatingDTO ratingDTO,
            BindingResult result,
            Model model) {

        Course course = courseService.findById(id);

        if (course == null) {
            return "redirect:/courses";
        }

        model.addAttribute("course", course);

        if (result.hasErrors()) {
            return "views/courses/course-add-rating";
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId;
        if (principal instanceof AppPrincipal appPrincipal) {
            userId = appPrincipal.getId();
        } else {
            throw new RuntimeException("Unbekannter Principal-Typ");
        }

        User currentUser = userService.getUserById(userId);

        Rating newRating = new Rating();
        newRating.setStars(ratingDTO.getStars());
        newRating.setComment(ratingDTO.getComment());
        newRating.setCourse(course);
        newRating.setAuthor(currentUser);

        ratingService.save(newRating);

        System.out.println("Rating saved with ID: " + newRating.getId());

        return "redirect:/courses/" + id;
    }


}