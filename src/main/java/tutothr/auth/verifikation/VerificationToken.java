package tutothr.auth.verifikation;

import jakarta.persistence.*;
import tutothr.user.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class VerificationToken {

    @Id
    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private LocalDateTime expiryDate;

    public VerificationToken() {}

    public VerificationToken(User user) {
        this.user = user;
        this.expiryDate = LocalDateTime.now().plusMinutes(10); // Token 10 minutes gültig
        this.token = UUID.randomUUID().toString();
    }

    // Getter und Setter hier einfügen
    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiryDate);
    }
}
