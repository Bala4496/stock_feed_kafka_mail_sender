package ua.bala.stock_feed_kafka_mail_sender.model.messages;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RegisterUserMessage {
    String firstName;
    String lastName;
    String email;
    String token;
}
