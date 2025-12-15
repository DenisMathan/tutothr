package tutothr.chapter;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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
    public String putChapter(@ModelAttribute Chapter chapter, BindingResult result, @PathVariable Long id, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        if (result.hasErrors()) {
            return "redirect:/tutor/courses";
        }
        chapterService.update(chapter);
        return "redirect:" + referer;
    }
}
