package ua.bala.stock_feed_kafka_mail_sender.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ua.bala.stock_feed_kafka_mail_sender.model.entity.EmailEventLog;
import ua.bala.stock_feed_kafka_mail_sender.model.enums.EmailType;
import ua.bala.stock_feed_kafka_mail_sender.model.messages.RegisterUserMessage;
import ua.bala.stock_feed_kafka_mail_sender.repository.EventLogRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventLogServiceImpl implements EventLogService {

    private final EventLogRepository eventLogRepository;

    @Override
    public void logRegisterEmailEvent(RegisterUserMessage message) {
        log.info("Logging Register Email Event for {}", message.getEmail());
        Mono.just(message)
                .map(mes -> new EmailEventLog()
                        .setEmailType(EmailType.REGISTER_EMAIL)
                        .setEmail(message.getEmail())
                        .setFirstName(message.getFirstName())
                        .setLastName(message.getLastName())
                        .setToken(message.getToken())
                )
                .flatMap(eventLogRepository::save)
                .subscribe();
    }
}
