package tutothr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import tutothr.service.impl.MyUserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private MyUserDetailsServiceImpl userDetailsService;

    public SecurityConfig(MyUserDetailsServiceImpl myUserDetailsServiceImpl) {
        this.userDetailsService = myUserDetailsServiceImpl;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**", "/h2-console/**"));

        http.headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin));
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/resources/**", "/api/**", "/api/workshops/**", "/student/**",
                        "/webjars/**", "/h2-console/**", "/login", "/register", "/logout", "/404", "/all")
                .permitAll()
                .requestMatchers("/home").authenticated()
                // .requestMatchers("/all").hasRole("ADMIN")
                .requestMatchers("/registration/**").hasAuthority("REGISTRATION"));

        // http.formLogin(Customizer.withDefaults());
        http.formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login") // POST /login wird verarbeitet
                .defaultSuccessUrl("/home", true) // nach Login: SavedRequest oder /home
                .failureUrl("/login?error") // bei Fehler
                .permitAll());
        http.logout(logout -> logout
                .logoutUrl("/logout") // default
                .logoutSuccessUrl("/login?logout") // Ziel nach Logout
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll());
        // http.httpBasic(Customizer.withDefaults());

        http.exceptionHandling(ex -> ex.accessDeniedPage("/404"));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
