package com.mafasoft.notifications.service;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import lombok.Data;

@Entity
@Table(name = "notification_resource")
@Data
class Resource {

    @EmbeddedId
    private ResourceId resourceId;

    private ZonedDateTime nextAttemptOn;

    private boolean blocked;

    protected Resource() {}

    Resource(ResourceId resourceId) {
        this.resourceId = resourceId;
        this.blocked = false;
    }

    void nextAttemptOn(ZonedDateTime nextAttemptOn) {
        this.nextAttemptOn = nextAttemptOn;
    }

    void block() {
        this.blocked = true;
    }
}
