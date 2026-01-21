package tutothr.common.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import tutothr.booking.BookingDTO;
import tutothr.booking.invoice.InvoiceDTO;

@Service
public class MailService {
    @Autowired
    private JavaMailSender mailSender;
    @Value("${base-url}")
    private String baseUrl;
    @Value("${spring.mail.username}")
    private String mail;
    @Value("${spring.mail.password}")
    private String mailPw;

    private static final String WELCOME_MESSAGE_TEMPLATE = "Herzlich willkommen bei TutOTHr, %s!\n\n" +
            "Wir freuen uns sehr, dass du dich registriert hast und Teil unserer Lern-Community wirst.\n" +
            "Bei Fragen oder Problemen stehen wir dir jederzeit gerne zur Verfügung.\n\n" +
            "Viel Spaß und Erfolg beim Lernen!\n" +
            "Dein TutOTHr-Team";

    public void sendRegistrationMail(String to, String subject, String username) {
        if(mail == null || mailPw == null) {
            throw new IllegalStateException("Mail credentials are not set in environment variables.");
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(String.format(WELCOME_MESSAGE_TEMPLATE, username));
        mailSender.send(message);
    }
    public void sendVerificationEmail(tutothr.user.User user, String token) {
        String verificationLink = baseUrl + "/verify/token/" + token;

        if(mail == null || mailPw == null) {
            throw new IllegalStateException("Mail credentials are not set in environment variables.");
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("E-Mail Verifikation für TutOTHr");
        message.setText("Hallo " + user.getUsername() + ",\n\n" +
                "bitte klicke auf den folgenden Link, um deine E-Mail-Adresse zu verifizieren:\n" +
                verificationLink + "\n\n" +
                "Falls du dich nicht bei TutOTHr registriert hast, ignoriere diese E-Mail einfach.\n\n" +
                "Viele Grüße,\n" +
                "Dein TutOTHr-Team");

        mailSender.send(message);
    }

    public void sendTwoFactorCode(tutothr.user.User user, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Dein Login-Code für TutOTHr");
        message.setText("Hallo " + user.getUsername() + ",\n\n" +
                "dein Bestätigungscode für den Login lautet:\n\n" +
                code + "\n\n" +
                "Dieser Code ist für diesen Login-Vorgang gültig.\n" +
                "Gib ihn bitte nicht weiter.\n\n" +
                "Viele Grüße,\n" +
                "Dein TutOTHr-Team");

        mailSender.send(message);
    }

    public void sendNewChatMail(tutothr.user.User user, tutothr.user.User sender) {
        if(mail == null || mailPw == null) {
            throw new IllegalStateException("Mail credentials are not set in environment variables.");
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Du hast eine neue Chatanfrage!");
        message.setText("Hallo " + user.getUsername() + " \n\n" +
                "du hast eine neue Chatanfrage von " + sender.getUsername() + ". \n" +
                "Schau mal in deinen Posteingang :) \n\n" +
                "Viele Grüße,\n" +
                "Dein TutOTHr-Team");
        mailSender.send(message);
    }

    public void sendBookingConfirmationToStudent(BookingDTO booking, InvoiceDTO invoice) {
    	String text = "Hallo " + booking.getStudentName() + ",\n\n" +
                "deine Buchung wurde erfolgreich abgeschlossen!\n\n" +
                "Details:\n" +
                "- " + booking.getBookingDescription() + "\n" +
                "- Preis: " + String.format("%.2f", booking.getPrice()) + " EUR\n" +
                "- Rechnungsnummer: " + invoice.getInvoiceNumber() + "\n\n" +
                "Viel Erfolg beim Lernen!\n" +
                "Dein TutOTHr-Team";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(booking.getStudentEmail());
        message.setSubject("Buchungsbestaetigung - TutOTHr");
        message.setText(text);

        mailSender.send(message);
    }

    public void sendBookingConfirmationToTutor(BookingDTO booking) {
    	String text = "Hallo " + booking.getTutorName() + ",\n\n" +
                "du hast eine neue Buchung erhalten!\n\n" +
                "Details:\n" +
                "- " + booking.getBookingDescription() + "\n" +
                "- Student: " + booking.getStudentName() + "\n" +
                "- Einnahme: " + String.format("%.2f", booking.getPrice()) + " EUR\n\n" +
                "Viele Gruesse\n" +
                "Dein TutOTHr-Team";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(booking.getTutorEmail());
        message.setSubject("Neue Buchung - TutOTHr");
        message.setText(text);

        mailSender.send(message);
    }
}