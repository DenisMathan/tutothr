package tutothr.message.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;

@Configuration
@EnableWebSocketSecurity
public class WebSocketSecurityConfig {

    @Bean
    public AuthorizationManager<Message<?>> messageAuthorizationManager(
            MessageMatcherDelegatingAuthorizationManager.Builder messages) {

        messages
                // 1. Technische Nachrichten (Connect/Disconnect) immer erlauben
                .nullDestMatcher().permitAll()
                .simpTypeMatchers(SimpMessageType.CONNECT, SimpMessageType.DISCONNECT, SimpMessageType.HEARTBEAT).permitAll()

                // 2. Chat-Nachrichten senden (/app/...)
                .simpDestMatchers("/app/**").authenticated()

                // 3. Auf eigene Queue hören (/user/...)
                .simpSubscribeDestMatchers("/user/**").authenticated()

                // 4. Alles andere verbieten
                .anyMessage().denyAll();

        return messages.build();
    }

    // Überschreibt den Standard-CSRF-Interceptor.
    // Verhindert, dass CONNECT Frames geblockt werden, weil kein X-XSRF-TOKEN Header dabei ist.
    @Bean
    public ChannelInterceptor csrfChannelInterceptor() {
        return new ChannelInterceptor() {};
    }
}