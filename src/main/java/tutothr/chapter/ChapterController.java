package tutothr.chapter;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PutMapping;

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

    @PutMapping("tutor/chapters/save/{id}")
    public String putChapter(@ModelAttribute @Valid ChapterDTO chapterDTO, BindingResult result, @PathVariable Long id, HttpServletRequest request, Model model) {
        // String referer = request.getHeader("Referer");
        Chapter _chapter =  chapterService.findById(id);
        if (result.hasErrors()) {
            chapterDTO = chapterService.handleValidationErrors(chapterDTO, result.getFieldErrors());
            model.addAttribute("course", _chapter.getCourse());
            return "/views/courses/course";
        }
        chapterService.update(chapterDTO);
        return "redirect:/courses/" + _chapter.getCourse().getId();
    }
}
