package tutothr.course;

import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tutothr.common.config.MyUserDetails;

@Service
public class CoursePermissionService {

    public boolean isCurrentUserOwner(Long courseOwnerId) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();
        return userId.equals(courseOwnerId);
    }
    public List<Course> setOwner(List<Course> items) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();
        for (Course course : items) {
            course.setIsOwner(userId.equals(course.getOwnerId()));
        }
        return items;
    }
}
