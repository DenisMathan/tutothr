package tutothr.chapter;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpServletRequest;

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
        chapterService.deleteChapterById(id);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @PutMapping("tutor/chapters/save/{id}")
    public String putChapter(@ModelAttribute Chapter chapter, BindingResult result, @PathVariable Long id, HttpServletRequest request, Model model) {
        // String referer = request.getHeader("Referer");
        Chapter _chapter =  chapterService.findById(id);
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                _chapter.addValidationError(error.getField(), error.getDefaultMessage());
            }
            _chapter.getCourse().setIsOwner(true);
            model.addAttribute("course", _chapter.getCourse());
            return "/views/courses/course";
        }
        chapterService.update(chapter);
        return "redirect:/courses/" + _chapter.getCourse().getId();
    }
}
