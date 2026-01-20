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
    
    public boolean isOwnerOrAdmin(Long courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal().equals("anonymousUser")) {
            return false;
        }
       System.out.println("Checking permissions for user: " + authentication.getDetails()); 
        // Check for ADMIN role
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return true;
        }

        // Check ownership
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            return false; // Or throw EntityNotFoundException, but boolean is safer for PreAuthorize
        }
        
        return isCurrentUserOwner(course.getOwnerId());
    }
}
