package ua.bala.stock_feed_kafka_mail_sender.model.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import ua.bala.stock_feed_kafka_mail_sender.model.enums.EmailType;

import java.time.LocalDateTime;

@Table("email_event_logs")
@Data
@Accessors(chain = true)
public class EmailEventLog {

    @Id
    private Long id;
    @Column("email_type")
    private EmailType emailType;
    private String email;
    @Column("first_name")
    private String firstName;
    @Column("last_name")
    private String lastName;
    private String token;
    @CreatedDate
    private LocalDateTime createdAt;
}
