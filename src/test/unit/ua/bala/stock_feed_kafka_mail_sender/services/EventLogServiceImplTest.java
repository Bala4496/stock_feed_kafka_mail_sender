package ua.bala.stock_feed_kafka_mail_sender.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import ua.bala.stock_feed_kafka_mail_sender.model.entity.EmailEventLog;
import ua.bala.stock_feed_kafka_mail_sender.model.enums.EmailType;
import ua.bala.stock_feed_kafka_mail_sender.model.messages.RegisterUserMessage;
import ua.bala.stock_feed_kafka_mail_sender.repository.EventLogRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class EventLogServiceImplTest {

    @InjectMocks
    private EventLogServiceImpl eventLogService;
    @Mock
    private EventLogRepository eventLogRepository;

    @Test
    void logRegisterEmailEvent() {
        var message = getTestRegisterUserMessage();
        when(eventLogRepository.save(any(EmailEventLog.class))).thenReturn(Mono.just(new EmailEventLog()));

        eventLogService.logRegisterEmailEvent(message);

        verify(eventLogRepository).save(argThat(emailEventLog ->
                emailEventLog.getEmailType() == EmailType.REGISTER_EMAIL
                && emailEventLog.getFirstName().equals("FirstName")
                && emailEventLog.getLastName().equals("LastName")
                && emailEventLog.getEmail().equals("test.account@gmail.com")
                && emailEventLog.getToken().equals("super_secret_test_token"))
        );
    }

    private static RegisterUserMessage getTestRegisterUserMessage() {
        return new RegisterUserMessage()
                .setFirstName("FirstName")
                .setLastName("LastName")
                .setEmail("test.account@gmail.com")
                .setToken("super_secret_test_token");
    }
}