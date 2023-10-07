package ua.bala.stock_feed_kafka_mail_sender.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ua.bala.stock_feed_kafka_mail_sender.model.entity.EmailEventLog;

public interface EventLogRepository extends ReactiveCrudRepository<EmailEventLog, Long> {
}
