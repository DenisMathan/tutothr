package tutothr.chapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import tutothr.course.CourseDTO;

@Controller
public class ChapterController {

    private static final Logger logger = LoggerFactory.getLogger(ChapterController.class);

    private final ChapterService chapterService;
    public ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }

    @DeleteMapping("/tutor/chapters/delete/{id}")
    @PreAuthorize("@chapterPermissionService.canEditChapter(#id)")
    public String deleteChapter(HttpServletRequest request, @PathVariable Long id) {
        if (id == null) {
            return "redirect:/tutor/courses";
        }
        chapterService.deleteById(id);
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/tutor/courses");
    }

    @PostMapping("/tutor/chapters/save")
    @PreAuthorize("@chapterPermissionService.canEditCourseChapters(#chapterDTO.courseId)")
    public String createChapter(@ModelAttribute @Valid ChapterDTO chapterDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            CourseDTO course = chapterService.findCourseDTOById(chapterDTO.getCourseId());
            chapterDTO = chapterService.handleValidationErrors(chapterDTO, result.getFieldErrors());
            // Make sure the course view knows we are adding a chapter
            course.setAddChapter(chapterDTO);
            model.addAttribute("course", course);
            return "/views/courses/course";
        }
        
        logger.debug("Saving chapter for course: {}", chapterDTO.getCourseId());
        chapterService.saveDTO(chapterDTO);
        return "redirect:/courses/" + chapterDTO.getCourseId();
    }
    
    @PutMapping("/tutor/chapters/save/{id}")
    @PreAuthorize("@chapterPermissionService.canEditChapter(#id)")
    public String updateChapter(@ModelAttribute @Valid ChapterDTO chapterDTO, BindingResult result, @PathVariable Long id, Model model) {
        Chapter existingChapter = chapterService.findById(id);
        if (existingChapter == null) {
             return "redirect:/courses"; // or 404
        }
        
        if (result.hasErrors()) {
            chapterDTO = chapterService.handleValidationErrors(chapterDTO, result.getFieldErrors());
            chapterDTO.setId(id);
            
            // Restore fields that are not in the form but needed for display
            chapterDTO.setPaywalled(existingChapter.isPaywalled()); // if paywalled state is intrinsic/unchangeable here, otherwise it should be in form
            chapterDTO.setPosition(existingChapter.getPosition());
            
            final ChapterDTO errorChapter = chapterDTO;
            CourseDTO course = chapterService.findCourseDTOById(existingChapter.getCourse().getId());
            
            // Replaces the chapter in the list with the one containing errors/user input
            course.setChapters(course.getChapters().stream()
                    .map(c -> c.getId().equals(id) ? errorChapter : c)
                    .toList());
            
            model.addAttribute("course", course);
            return "/views/courses/course";
        }
        
        chapterService.update(chapterDTO);
        return "redirect:/courses/" + existingChapter.getCourse().getId();
    }

    @DeleteMapping("/tutor/chapters/deleteAttachment/{chapterId}/{attachmentIndex}")
    @PreAuthorize("@chapterPermissionService.canEditChapter(#chapterId)")
    public String deleteAttachment(HttpServletRequest request, @PathVariable Long chapterId, @PathVariable int attachmentIndex) {
        chapterService.deleteAttachment(chapterId, attachmentIndex);
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/tutor/courses");
    }
}
