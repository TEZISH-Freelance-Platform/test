package tezish.tezish.models.Log;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_logs")
@Data
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String body;

    private String status;
    @Column(columnDefinition = "TEXT")
    private String response;
    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    private LocalDateTime sentAt;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isOpened;

    public NotificationLog() {
        this.isOpened = false;
    }

}

