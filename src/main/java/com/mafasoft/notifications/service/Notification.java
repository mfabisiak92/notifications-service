package com.mafasoft.notifications.service;

import static jakarta.persistence.EnumType.STRING;

import com.mafasoft.notifications.service.dto.CreateNotificationDto;
import com.mafasoft.notifications.common.NotificationType;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "notifications")
@Data
@ToString(of = {"notificationId", "notificationType", "resource"})
class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private final UUID notificationId = UUID.randomUUID();

    private ZonedDateTime createdAt;

    private int attempts;

    private ZonedDateTime nextAttemptOn;

    private ZonedDateTime sentOn;

    @Enumerated(STRING)
    private NotificationType notificationType;

    @Enumerated(STRING)
    private NotificationStatus status;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "resource_id", nullable = false),
            @JoinColumn(name = "resource_type", nullable = false)
    })
    private Resource resource;

    @Type(JsonType.class)
    @Column(name = "content", columnDefinition = "jsonb")
    private Object content;

    static Notification create(CreateNotificationDto dto) {
        return new Notification(
                ZonedDateTime.now(),
                dto.notificationType(),
                new Resource(new ResourceId(dto.resourceId(), dto.resourceType())),
                dto.content());
    }

    protected Notification() {}

    private Notification(
            ZonedDateTime createdAt, NotificationType notificationType, Resource resource, Object content) {
        this.createdAt = createdAt;
        this.attempts = 0;
        this.nextAttemptOn = createdAt;
        this.notificationType = notificationType;
        this.status = NotificationStatus.PENDING;
        this.resource = resource;
        this.content = content;
    }

    void schedule() {
        this.status = NotificationStatus.SCHEDULED;
    }

    void nextAttemptOn(ZonedDateTime nextAttemptOn) {
        this.nextAttemptOn = nextAttemptOn;
        this.resource.nextAttemptOn(nextAttemptOn);
    }

    void sent() {
        this.attempts++;
        this.status = NotificationStatus.SENT;
        this.sentOn = ZonedDateTime.now();
    }

    void notSent() {
        this.attempts++;
    }

    void resetScheduledToPending() {
        if (this.status == NotificationStatus.SCHEDULED) {
            this.status = NotificationStatus.PENDING;
        }
    }
}
