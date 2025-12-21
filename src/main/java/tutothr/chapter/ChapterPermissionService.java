package tutothr.chapter;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import tutothr.auth.config.AppPrincipal;

@Service
public class ChapterPermissionService {
    public boolean isCurrentUserOwner(Long chapterOwnerId) {
        AppPrincipal principal = (AppPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = principal.getId();
        return userId.equals(chapterOwnerId);
    }

}
