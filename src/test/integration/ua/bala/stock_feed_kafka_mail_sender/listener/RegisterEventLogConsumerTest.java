package ua.bala.stock_feed_kafka_mail_sender.listener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.context.ActiveProfiles;
import ua.bala.stock_feed_kafka_mail_sender.BaseIT;
import ua.bala.stock_feed_kafka_mail_sender.model.messages.RegisterUserMessage;
import ua.bala.stock_feed_kafka_mail_sender.services.EmailService;
import ua.bala.stock_feed_kafka_mail_sender.services.EventLogService;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(topics = "stock-feed-topic")
public class RegisterEventLogConsumerTest extends BaseIT {

    EmbeddedKafkaBroker embeddedKafkaBroker = new EmbeddedKafkaBroker(1);
    KafkaListenerEndpointRegistry endpointRegistry = new KafkaListenerEndpointRegistry();

    @Autowired
    KafkaTemplate<Long, RegisterUserMessage> kafkaTemplate;

    @SpyBean
    RegisterUserMessageListener registerUserMessageListener;
    @SpyBean
    EmailService emailService;
    @SpyBean
    EventLogService eventLogService;
    @Captor
    ArgumentCaptor<RegisterUserMessage> messageArgumentCaptor;

    @BeforeEach
    void setUp() {
        for (MessageListenerContainer messageListenerContainer: endpointRegistry.getListenerContainers()) {
            ContainerTestUtils.waitForAssignment(messageListenerContainer, embeddedKafkaBroker.getPartitionsPerTopic());
        }
    }

    @Test
    public void shouldListenForRegistrationMessages() throws ExecutionException, InterruptedException {
        var message = getTestRegisterUserMessage();
        doNothing().when(emailService).sendRegistrationEmail(any(RegisterUserMessage.class));
        doNothing().when(eventLogService).logRegisterEmailEvent(any(RegisterUserMessage.class));

        var sendMessage = kafkaTemplate.sendDefault(message).get().getProducerRecord().value();
        assertNotNull(sendMessage);

        TimeUnit.SECONDS.sleep(10);

        verify(registerUserMessageListener).listenForRegistration(isA(RegisterUserMessage.class));
        verify(registerUserMessageListener).listenForRegistration(messageArgumentCaptor.capture());
        var receivedMessage = messageArgumentCaptor.getValue();
        assertNotNull(receivedMessage);


        assertEquals(receivedMessage.getFirstName(), "FirstName");
        assertEquals(receivedMessage.getLastName(), "LastName");
        assertEquals(receivedMessage.getEmail(), "test.account@gmail.com");
        assertEquals(receivedMessage.getToken(), "super_secret_test_token");
    }

    private static RegisterUserMessage getTestRegisterUserMessage() {
        return new RegisterUserMessage()
                .setFirstName("FirstName")
                .setLastName("LastName")
                .setEmail("test.account@gmail.com")
                .setToken("super_secret_test_token");
    }
}
