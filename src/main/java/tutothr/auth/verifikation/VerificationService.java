package tutothr.auth.verifikation;

import org.springframework.stereotype.Service;
import tutothr.user.User;
import tutothr.user.UserService;
import tutothr.common.services.MailService;

@Service
public class VerificationService{
    private MailService mailService;
    private VerificationRepositoryI repository;
    private UserService userService;
    public VerificationService (VerificationRepositoryI repository, MailService mailService, UserService userService){
        this.mailService = mailService;
        this.repository = repository;
        this.userService = userService;
    }

    public boolean userTokenExpired(Long userId) {
        VerificationToken token = repository.findByUserId(userId).orElse(null);
        if (token == null) {
            return true; // Kein Token gefunden, also als abgelaufen betrachten
        }
        return token.isExpired();
    }
    public VerificationToken createToken(User user) {
        VerificationToken token = new VerificationToken(user);
        repository.save(token);
        return token;
    }

    public boolean sendVerificationEmail(Long userId) {
        VerificationToken verificationToken = repository.findByUserId(userId).orElse(null);
        User user;
        if (verificationToken != null) {
            repository.delete(verificationToken);
            user = verificationToken.getUser();
        } else {
            user = userService.getUserById(userId);
        }
        if(user == null) {
            return  false;
        }
        VerificationToken newToken = new VerificationToken(user);
        repository.save(newToken);
        mailService.sendVerificationEmail(user, newToken.getToken());
        return true;
    }

    public User verifyToken(String tokenStr) {
        VerificationToken token = repository.findById(tokenStr).orElse(null);
        if (token == null) {
            return null; // Token nicht gefunden oder abgelaufen
        }
        if(token.isExpired()) {
            repository.delete(token);
            return null;
        }
        User user = token.getUser();
        user.setVerified(true);
        user = userService.saveUser(user);
        repository.delete(token); // Token nach erfolgreicher Verifikation löschen
        return user;
    }
    public User getUserById(Long userId) {
        return userService.getUserById(userId);
    }
}
