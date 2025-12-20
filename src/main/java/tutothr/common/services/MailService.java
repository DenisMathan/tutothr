package tutothr.common.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    @Autowired
    private JavaMailSender mailSender;

  private static final String WELCOME_MESSAGE_TEMPLATE =
    "Herzlich willkommen bei TutOTHr, %s!\n\n" +
    "Wir freuen uns sehr, dass du dich registriert hast und Teil unserer Lern-Community wirst.\n" +
    "Bei Fragen oder Problemen stehen wir dir jederzeit gerne zur Verfügung.\n\n" +
    "Viel Spaß und Erfolg beim Lernen!\n" +
    "Dein TutOTHr-Team";

    public void sendRegistrationMail(String to, String subject, String username) {
        String mail = System.getenv("MAIL");
        String mailPw = System.getenv("MAILPW");
        if(mail == null || mailPw == null) {
            throw new IllegalStateException("Mail credentials are not set in environment variables.");
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(String.format(WELCOME_MESSAGE_TEMPLATE, username));
        mailSender.send(message);
    }
}