package tutothr.chapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tutothr.auth.config.AppPrincipal;
import tutothr.chapter.interfaces.ChapterRepositoryI;
import tutothr.course.interfaces.CourseRepositoryI;

@Service("chapterPermissionService") // Explicit name for SpEL
public class ChapterPermissionService {

    private static final Logger logger = LoggerFactory.getLogger(ChapterPermissionService.class);

    private final ChapterRepositoryI chapterRepository;
    private final CourseRepositoryI courseRepository;

    public ChapterPermissionService(ChapterRepositoryI chapterRepository, CourseRepositoryI courseRepository) {
        this.chapterRepository = chapterRepository;
        this.courseRepository = courseRepository;
    }

    /**
     * Prüft, ob der eingeloggte User der Besitzer des Kurses (dem das Kapitel gehört) ist.
     * @param courseOwnerId Die ID des Besitzers (User ID).
     */
    public boolean isCurrentUserOwner(Long courseOwnerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return false;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof AppPrincipal) {
            Long userId = ((AppPrincipal) principal).getId();
            return userId != null && userId.equals(courseOwnerId);
        }
        return false;
    }

    /**
     * Prüft, ob der aktuelle User die Berechtigung hat, ein Kapitel zu erstellen/bearbeiten/löschen.
     * Dies ist erlaubt, wenn der User ADMIN ist ODER (TUTOR UND Besitzer des Kurses).
     * 
     * @param courseId Die ID des Kurses, zu dem das Kapitel gehört.
     */
    @Transactional(readOnly = true)
    public boolean canEditCourseChapters(Long courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            logger.debug("Access denied: User is anonymous");
            return false;
        }

        // 1. ADMIN check
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            logger.debug("Access granted: User is ADMIN");
            return true;
        }

        // 2. TUTOR check
        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_TUTOR"))) {
            logger.debug("Access denied: User is NOT TUTOR (and not ADMIN)");
            return false;
        }

        // 3. OWNER check
        return courseRepository.findById(courseId)
                .map(course -> {
                    boolean isOwner = isCurrentUserOwner(course.getOwner().getId());
                    if (!isOwner) {
                        logger.debug("Access denied: Tutor {} is not owner of course {}", 
                                authentication.getName(), courseId);
                    }
                    return isOwner;
                })
                .orElseGet(() -> {
                    logger.debug("Access denied: Course {} not found", courseId);
                    return false;
                });
    }

    /**
     * Prüft, ob das Kapitel bearbeitet werden darf.
     * Ermittelt den Kurs anhand der Chapter-ID.
     */
    @Transactional(readOnly = true)
    public boolean canEditChapter(Long chapterId) {
        return chapterRepository.findById(chapterId)
                .map(chapter -> canEditCourseChapters(chapter.getCourse().getId()))
                .orElseGet(() -> {
                     logger.debug("Access denied: Chapter {} not found", chapterId);
                     return false;
                });
    }
}
