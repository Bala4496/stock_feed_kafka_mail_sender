package ua.bala.stock_feed_kafka_mail_sender.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.bala.stock_feed_kafka_mail_sender.model.messages.RegisterUserMessage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class EmailServiceImplTest {

    @InjectMocks
    private EmailServiceImpl emailService;
    @Mock
    private JavaMailSender mailSender;

    @Test
    void shouldSendRegistrationEmail() {
        var message = getTestRegisterUserMessage();
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        emailService.sendRegistrationEmail(message);

        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    private static RegisterUserMessage getTestRegisterUserMessage() {
        return new RegisterUserMessage()
                .setFirstName("FirstName")
                .setLastName("LastName")
                .setEmail("test.account@gmail.com")
                .setToken("super_secret_test_token");
    }
}
