package tutothr.common.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import tutothr.common.utils.MyUtils;

public class RedirectCheckFilter extends OncePerRequestFilter {

    // Diese Pfade sind immer erlaubt, auch ohne Username (damit CSS, JS und Logout
    // funktionieren)
    private static final List<String> ALLOWED_PATHS = Arrays.asList(
            "/admin/all",
            "/set-username",
            "/logout",
            "/login",
            "/error",
            "/css/",
            "/js/",
            "/image/",
            "/webjars/",
            "/favicon.ico");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        String path = request.getRequestURI();
        boolean isAllowed = ALLOWED_PATHS.stream().anyMatch(path::startsWith);
        if (isAllowed) {
            filterChain.doFilter(request, response);
            return;
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (MyUtils.isGoogleLogin(auth)) {
            if (MyUtils.missingUsername(auth)) {
                response.sendRedirect("/set-username");
                return; 
            }
        }
        filterChain.doFilter(request, response);
    }
}
