package tutothr.course;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import tutothr.auth.config.AppPrincipal;

@Service
public class CoursePermissionService {

    public boolean isCurrentUserOwner(Long courseOwnerId) {
        AppPrincipal principal = (AppPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = principal.getId();
        return userId.equals(courseOwnerId);
        
    }
}
