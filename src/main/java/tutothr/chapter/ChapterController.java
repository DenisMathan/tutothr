package tutothr.chapter;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import tutothr.course.CourseDTO;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class ChapterController {

    private ChapterService chapterService;

    public ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }

    @DeleteMapping("/tutor/chapters/delete/{id}")
    public String deleteChapter(HttpServletRequest request, @PathVariable Long id) {
        if (id == null) {
            return "redirect:/tutor/courses";
        }
        chapterService.deleteById(id);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @PostMapping("/tutor/chapters/save")
    public String createCategory(@ModelAttribute @Valid ChapterDTO chapterDTO, BindingResult result, HttpServletRequest request, Model model) {
        if (result.hasErrors()) {
            CourseDTO course =  chapterService.findCourseDTOById(chapterDTO.getCourseId());
            chapterDTO = chapterService.handleValidationErrors(chapterDTO, result.getFieldErrors());
            course.setAddChapter(chapterDTO);
            model.addAttribute("course", course);
            return "/views/courses/course";
        }
        System.out.println("Saving chapter:" + chapterDTO.getCourseId());
        System.out.println(chapterService.findCourseDTOById(chapterDTO.getCourseId()).getId());
        chapterService.saveDTO(chapterDTO);
        return "redirect:/courses/" + chapterDTO.getCourseId();
    }
    

    @PutMapping("tutor/chapters/save/{id}")
    public String putChapter(@ModelAttribute @Valid ChapterDTO chapterDTO, BindingResult result, @PathVariable Long id, HttpServletRequest request, Model model) {
        // String referer = request.getHeader("Referer");
        Chapter _chapter =  chapterService.findById(id);
        if (result.hasErrors()) {
            chapterDTO = chapterService.handleValidationErrors(chapterDTO, result.getFieldErrors());
            chapterDTO.setId(id);
            // Werte übernehmen, die nicht im Formular waren (z.B. paywalled), damit die Anzeige stimmt
            chapterDTO.setPaywalled(_chapter.isPaywalled());
            chapterDTO.setPosition(_chapter.getPosition());
            
            final ChapterDTO errorChapter = chapterDTO;
            CourseDTO course =  chapterService.findCourseDTOById(_chapter.getCourse().getId());
            course.setChapters(course.getChapters().stream().map(c -> c.getId().equals(id) ? errorChapter : c).toList());
            
            model.addAttribute("course", course);
            return "/views/courses/course";
        }
        chapterService.update(chapterDTO);
        return "redirect:/courses/" + _chapter.getCourse().getId();
    }
}
