package com.mafasoft.notifications.service;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;

class ResourceNotifications {
    private final Resource resource;
    private final List<Notification> notifications;

    private boolean blocked;

    ResourceNotifications(Resource resource, List<Notification> notifications) {
        this.resource = resource;
        this.notifications = notifications;
    }

    List<Notification> notifications() {
        return notifications;
    }

    boolean isBlocked() {
        return blocked;
    }

    ZonedDateTime firstCreatedAt() {
        return notifications.stream()
                .min(Comparator.comparing(Notification::getCreatedAt))
                .map(Notification::getCreatedAt)
                .orElseThrow();
    }

    void schedule() {
        this.notifications.forEach(Notification::schedule);
    }

    void block() {
        this.blocked = true;
        this.notifications.forEach(Notification::resetScheduledToPending);
    }
}
