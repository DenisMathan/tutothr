package tutothr.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import tutothr.auth.config.AppPrincipal;
import tutothr.course.interfaces.CourseRepositoryI;

@Service
public class CoursePermissionService {

    @Autowired
    private CourseRepositoryI courseRepository;

    public boolean isCurrentUserOwner(Long courseOwnerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal().equals("anonymousUser")) {
            return false;
        }
        AppPrincipal principal = (AppPrincipal) authentication.getPrincipal();
        Long userId = principal.getId();
        return userId.equals(courseOwnerId);
    }
    
    public boolean isTutorAndOwnerOrAdmin(Long courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal().equals("anonymousUser")) {
            System.out.println("DEBUG: User is anonymous");
            return false;
        }

        // 1. ADMIN check
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            System.out.println("DEBUG: User is ADMIN -> ALLOW");
            return true;
        }
        
        // 2. TUTOR check
        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_TUTOR"))) {
            System.out.println("DEBUG: User is NOT TUTOR -> DENY");
            return false;
        }

        // 3. OWNER check
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            System.out.println("DEBUG: Course " + courseId + " not found -> DENY");
            return false; 
        }
        boolean isOwner = isCurrentUserOwner(course.getOwnerId());
        
        return isOwner;
    }
}
