package tutothr.booking;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import tutothr.auth.config.MyUserDetails;
import tutothr.user.User;
import tutothr.user.UserService;

@Controller
@RequestMapping("/tutor/timeslots")
public class TimeSlotController {
	private final TimeSlotService timeSlotService;
	private final UserService userService;

	public TimeSlotController(TimeSlotService timeSlotService, UserService userService) {
		this.timeSlotService = timeSlotService;
		this.userService = userService;
	}

	@GetMapping
	public String listMyTimeSlots(@AuthenticationPrincipal MyUserDetails userDetails, Model model) {
		User tutor = userService.getUserById(userDetails.getId());
		model.addAttribute("timeslots", timeSlotService.findByTutor(tutor));
		model.addAttribute("newTimeslot", new TimeSlotDTO());
		return "views/booking/tutor-timeslots";
	}

	@PostMapping("/create")
	public String createTimeSlot(@AuthenticationPrincipal MyUserDetails userDetails, @ModelAttribute TimeSlotDTO dto) {
		dto.setTutorId(userDetails.getId());
		timeSlotService.save(dto);
		return "redirect:/tutor/timeslots";
	}
	
	@PostMapping("/delete/{id}")
	public String deleteTimeSlot(@PathVariable Long id) {
		timeSlotService.deleteById(id);
		return "redirect:/tutor/timeslots";
	}
}
