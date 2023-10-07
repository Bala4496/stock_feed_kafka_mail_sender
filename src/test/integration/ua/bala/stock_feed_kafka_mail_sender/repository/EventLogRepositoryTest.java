package ua.bala.stock_feed_kafka_mail_sender.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import ua.bala.stock_feed_kafka_mail_sender.BaseIT;
import ua.bala.stock_feed_kafka_mail_sender.model.entity.EmailEventLog;
import ua.bala.stock_feed_kafka_mail_sender.model.enums.EmailType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataR2dbcTest
public class EventLogRepositoryTest extends BaseIT {

    @Autowired
    private EventLogRepository eventLogRepository;

    @Test
    public void testSaveAndRetrieveEmailEventLog() {
        var emailEventLog = getTestRegisterEmailEventLog();

        eventLogRepository.save(emailEventLog).block();

        var savedEventLog = eventLogRepository.findById(emailEventLog.getId()).blockOptional();

        assertTrue(savedEventLog.isPresent());

        var retrievedEventLog = savedEventLog.get();

        assertEquals(EmailType.REGISTER_EMAIL, retrievedEventLog.getEmailType());
        assertEquals("FirstName", retrievedEventLog.getFirstName());
        assertEquals("LastName", retrievedEventLog.getLastName());
        assertEquals("test.account@gmail.com", retrievedEventLog.getEmail());
        assertEquals("super_secret_test_token", retrievedEventLog.getToken());
    }

    private static EmailEventLog getTestRegisterEmailEventLog() {
        return new EmailEventLog()
                .setEmailType(EmailType.REGISTER_EMAIL)
                .setFirstName("FirstName")
                .setLastName("LastName")
                .setEmail("test.account@gmail.com")
                .setToken("super_secret_test_token");
    }
}