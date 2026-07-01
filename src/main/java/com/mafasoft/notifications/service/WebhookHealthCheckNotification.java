package com.mafasoft.notifications.service;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static com.mafasoft.notifications.common.JsonSerializationConstants.DATE_TIME_FORMAT;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mafasoft.notifications.common.NotificationType;
import java.time.ZonedDateTime;
import java.util.UUID;

record WebhookHealthCheckNotification (
        UUID notificationId,
        @JsonFormat(shape = STRING, pattern = DATE_TIME_FORMAT, timezone = "UTC") ZonedDateTime createdAt,
        NotificationType notificationType,
        String content) {
    WebhookHealthCheckNotification(
            UUID notificationId, ZonedDateTime notificationDate, NotificationType notificationType) {
        this(notificationId, notificationDate, notificationType, "Webhook is working if this has been received.");
    }
}
