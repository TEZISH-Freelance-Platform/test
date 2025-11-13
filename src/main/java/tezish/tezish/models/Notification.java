package tezish.tezish.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    private String title;

    @Column(length = 2000)
    private String body;

    private LocalDateTime createdAt;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isOpened;

    public Notification() {
        this.isOpened = false;
    }

    public Notification(String adminNotification, String title, String body, LocalDateTime now) {
        this.type = adminNotification;
        this.title = title;
        this.body = body;
        this.createdAt = now;
        this.isOpened = false;
    }
}
