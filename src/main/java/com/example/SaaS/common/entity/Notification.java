package com.example.SaaS.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Notification target ID is required")
    @Positive(message = "Notification target ID must be positive")
    @Column(nullable = false, name = "notification_id")
    private Long notificationId;

    @NotBlank(message = "Notification type is required")
    @Size(max = 100, message = "Notification type must not exceed 100 characters")
    @Column(nullable = false, name = "notification_type")
    private String notificationType;

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters")
    @Column(nullable = false, name = "title")
    private String title;

    @NotBlank(message = "Body is required")
    @Size(min = 5, max = 2000, message = "Body must be between 5 and 2000 characters")
    @Column(nullable = false, name = "body")
    private String body;

    @Size(max = 50, message = "Notification category must not exceed 50 characters")
    @Column(name = "type")
    private String type;
    
    @Column(columnDefinition = "json")
    private String data;

    @NotNull(message = "Read status is required")
    @Column(nullable = false, name = "is_read")
    private Boolean isRead = false;

    @PastOrPresent(message = "Read date must be in the past or present")
    @Column(name = "read_at")
    private LocalDateTime readAt;

    @CreationTimestamp
    @PastOrPresent(message = "Created date must be in the past or present")
    @Column(updatable = false)
    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        if (isRead == null) {
            isRead = false;
        }
    }
}
