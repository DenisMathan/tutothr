package tutothr.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import tutothr.auth.CustomAuthenticationFailureHandler;
import tutothr.auth.CustomAuthenticationSuccessHandler;
import tutothr.auth.CustomOAuth2SuccessHandler;
import tutothr.auth.CustomOidcUserService;
import tutothr.auth.twoFactorVerification.TwoFactorVerificationFilter;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {
    public static final String[] PUBLIC_ENDPOINTS = {
            "/verify/**", "/admin/all", "/resources/**", "/css/**", "/api/**", "/api/workshops/**", "/webjars/**", "/h2-console/**",
            "/login",
            "/register", "/logout", "/404"
    };
    @Autowired
    private CustomOidcUserService customOidcUserService;
    @Autowired
    private CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    SecurityConfig(CustomOAuth2SuccessHandler customOAuth2SuccessHandler ) {
        this.customOAuth2SuccessHandler = customOAuth2SuccessHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        // return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/api/**", "/h2-console/**", "/ws/**"));
        http.headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin));
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                .requestMatchers("/home", "/student").authenticated()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/tutor/**").hasRole("TUTOR")
                .requestMatchers("/registration/**").hasAuthority("REGISTRATION")
                .anyRequest().authenticated());

        // regular Login form
        http.formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login") // POST /login wird verarbeitet
                .usernameParameter("email") // klingt dumm ist aber spring
                // .defaultSuccessUrl("/home", true) // nach Login: SavedRequest oder /home
                .successHandler(customAuthenticationSuccessHandler) // bei Erfolg
                .failureHandler(customAuthenticationFailureHandler) // bei Fehler
                .permitAll());

        // OAuth2 Login (Google)
        http.oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .userInfoEndpoint(userInfo -> userInfo
                        .oidcUserService(customOidcUserService))
                .successHandler(customOAuth2SuccessHandler)
                .failureHandler((request, response, exception) -> {
                    response.sendRedirect("/login?error");
                }));
        // Logout Konfiguration
        http.logout(logout -> logout
                .logoutUrl("/logout") // default
                .logoutSuccessUrl("/login?logout") // Ziel nach Logout
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll());
        
        // 2FA Filter
        http.addFilterBefore(new TwoFactorVerificationFilter(), UsernamePasswordAuthenticationFilter.class);

        // to Check if User needs to be redirected somehow
        http.addFilterAfter(new RedirectCheckFilter(PUBLIC_ENDPOINTS), AnonymousAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
