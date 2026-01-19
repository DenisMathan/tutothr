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
import tutothr.common.utils.MyUtils;

public class RedirectCheckFilter extends OncePerRequestFilter {
    private String[] PUBLIC_ENDPOINTS;

    public RedirectCheckFilter(String[] allowedPaths) {
        this.PUBLIC_ENDPOINTS = allowedPaths;

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        // 1. WebSocket-Verbindungen ignorieren
        if (path.startsWith("/ws")) {
            return true;
        }

        // 2. Statische Ressourcen ignorieren
        if (path.startsWith("/css") ||
                path.startsWith("/js") ||
                path.startsWith("/images") ||
                path.startsWith("/webjars") ||
                path.startsWith("/favicon")) {
            return true;
        }

        // 3. API-Endpunkte ignorieren
        if (path.startsWith("/api")) {
            return true;
        }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        String path = request.getRequestURI();
        
        // Prevent redirect loop if we are already on the target page
        if (path.equals("/set-username")) {
            filterChain.doFilter(request, response);
            return;
        }

        boolean isAllowed = Arrays.stream(PUBLIC_ENDPOINTS).anyMatch(path::startsWith);
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
