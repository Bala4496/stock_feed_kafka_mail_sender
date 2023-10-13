package ua.bala.stock_feed_kafka_mail_sender.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ua.bala.stock_feed_kafka_mail_sender.model.messages.RegisterUserMessage;
import ua.bala.stock_feed_kafka_mail_sender.services.EmailService;
import ua.bala.stock_feed_kafka_mail_sender.services.EventLogService;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegisterUserMessageListener {

    private final EmailService emailService;
    private final EventLogService eventLogService;

    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.group-id}")
    public void listenForRegistration(RegisterUserMessage message) {
        log.info("Message : {} - received", message);
        CompletableFuture.runAsync(() -> emailService.sendRegistrationEmail(message))
            .thenAccept((Void) -> eventLogService.logRegisterEmailEvent(message));
    }
}