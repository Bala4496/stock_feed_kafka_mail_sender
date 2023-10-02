package ua.bala.stock_feed_kafka_mail_sender.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ua.bala.stock_feed_kafka_mail_sender.model.messages.RegisterUserMessage;
import ua.bala.stock_feed_kafka_mail_sender.services.email.EmailService;

@Service
@RequiredArgsConstructor
public class RegisterUserMessageListener {

    private final EmailService emailService;

    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.group-id}")
    public void listenForRegistration(RegisterUserMessage message) {
        emailService.sendRegistrationEmail(message);
    }
}