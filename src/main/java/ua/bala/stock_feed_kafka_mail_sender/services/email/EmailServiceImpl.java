package ua.bala.stock_feed_kafka_mail_sender.services.email;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ua.bala.stock_feed_kafka_mail_sender.model.messages.RegisterUserMessage;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${server.port}")
    public int hostPort;
    public static final String HOST_URL = "http://localhost:%d";
    public static final String REGISTER_REGISTRATION_CONFIRM_TOKEN_API = "/api/v1/register/verify?token=%s";
    private final JavaMailSender mailSender;

    @Override
    public void sendRegistrationEmail(RegisterUserMessage message) {
        var simpleMailMessage = buildRegistrationConfirmationEmail(message.getFirstName(), message.getLastName(), message.getEmail(), message.getToken());
        mailSender.send(simpleMailMessage);
    }

    private SimpleMailMessage buildRegistrationConfirmationEmail(String firstName, String lastName, String email, String token) {
        var subject = "Registration Confirmation";
        var message = "Confirm, your email to finish registration on Stock Feed Viewer.";
        var confirmationUrl = HOST_URL.formatted(hostPort).concat(REGISTER_REGISTRATION_CONFIRM_TOKEN_API.formatted(token));
        return buildSimpleMessage(firstName, lastName, email, subject, message, confirmationUrl);
    }

    private static SimpleMailMessage buildSimpleMessage(String firstName, String lastName, String email, String subject, String message, String confirmationUrl) {
        var mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText("Dear, %s %s!\r\n%s\r\n%s".formatted(firstName, lastName, message, confirmationUrl));
        return mailMessage;
    }
}
