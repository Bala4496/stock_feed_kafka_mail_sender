package ua.bala.stock_feed_kafka_mail_sender.services;

import ua.bala.stock_feed_kafka_mail_sender.model.messages.RegisterUserMessage;

public interface EventLogService {

    void logRegisterEmailEvent(RegisterUserMessage message);
}
