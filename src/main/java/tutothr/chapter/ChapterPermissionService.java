package tutothr.chapter;

import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tutothr.common.config.MyUserDetails;

@Service
public class ChapterPermissionService {

    public boolean isCurrentUserOwner(Long chapterOwnerId) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();
        return userId.equals(chapterOwnerId);
    }

}
