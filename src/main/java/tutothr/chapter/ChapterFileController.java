package tutothr.chapter;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import tutothr.auth.config.AppPrincipal;
import tutothr.booking.ContentAccessService;
import tutothr.chapter.interfaces.ChapterRepositoryI;

@Controller
public class ChapterFileController {

    private final ChapterRepositoryI chapterRepository;
    private final ContentAccessService contentAccessService;
    private final Path uploadLocation;

    public ChapterFileController(ChapterRepositoryI chapterRepository, ContentAccessService contentAccessService) {
        this.chapterRepository = chapterRepository;
        this.contentAccessService = contentAccessService;
        this.uploadLocation = Paths.get("src/main/resources/static/uploads/chapters");
    }

    @GetMapping("/chapter/{chapterId}/file/{filename:.+}")
    public Object downloadFile(
            @PathVariable Long chapterId,
            @PathVariable String filename,
            @AuthenticationPrincipal AppPrincipal principal,
            RedirectAttributes redirectAttributes) {
        
        Chapter chapter = chapterRepository.findById(chapterId).orElse(null);
        if (chapter == null) {
            return "redirect:/404";
        }
        
        String expectedUrl = "/uploads/chapters/" + chapterId + "/" + filename;
        if (!chapter.getAttachmentUrls().contains(expectedUrl)) {
            return "redirect:/404";
        }
        
        boolean isOwner = principal != null && principal.getId().equals(chapter.getOwnerId());
        boolean hasAccess = false;
        
        if (isOwner) {
            hasAccess = true;
        } else if (principal != null) {
            hasAccess = contentAccessService.canAccessChapter(
                principal.getId(),
                chapterId,
                chapter.getCourse().getId(),
                chapter.isPaywalled()
            );
        } else if (!chapter.isPaywalled()) {
            hasAccess = true;
        }
        
        if (!hasAccess) {
            redirectAttributes.addFlashAttribute("accessDenied", true);
            redirectAttributes.addFlashAttribute("accessDeniedChapterId", chapterId);
            return "redirect:/courses/" + chapter.getCourse().getId();
        }
        
        try {
            Path filePath = uploadLocation.resolve(String.valueOf(chapterId)).resolve(filename);
            Resource resource = new UrlResource(filePath.toUri());
            
            if (!resource.exists() || !resource.isReadable()) {
                return "redirect:/404";
            }
            
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            
            String downloadFilename = filename;
            if (filename.contains("_")) {
                downloadFilename = filename.substring(filename.indexOf("_") + 1);
            }
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                            "inline; filename=\"" + URLEncoder.encode(downloadFilename, StandardCharsets.UTF_8) + "\"")
                    .body(resource);
                    
        } catch (IOException e) {
            return "redirect:/404";
        }
    }
}