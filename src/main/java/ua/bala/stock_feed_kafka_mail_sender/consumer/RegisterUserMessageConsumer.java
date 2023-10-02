package ua.bala.stock_feed_kafka_mail_sender.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ua.bala.stock_feed_kafka_mail_sender.model.messages.RegisterUserMessage;
import ua.bala.stock_feed_kafka_mail_sender.services.EmailService;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegisterUserMessageConsumer {

    private final EmailService emailService;

    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.group-id}")
    public void listenForRegistration(RegisterUserMessage message) {
        log.info("Message : {} - received", message);
        emailService.sendRegistrationEmail(message);
    }
}