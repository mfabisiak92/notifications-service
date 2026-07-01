package com.mafasoft.notifications.service;

import com.mafasoft.notifications.common.NotificationType;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;

@Slf4j
class WebhookHealthIndicator implements HealthIndicator {

    private final WebhookClient webhookClient;

    private Health previousHealth;
    private Health health;

    WebhookHealthIndicator(WebhookClient webhookClient) {
        this.webhookClient = webhookClient;
        this.previousHealth = Health.unknown().build();
        this.health = Health.unknown().build();
    }

    public void checkHealth() {
        try {
            this.previousHealth = this.health;
            this.health = getHealth();
        } catch (Exception ex) {
            log.error("Exception while checking webhook health. Error message: {}", ex.getMessage());
            this.health = Health.down(ex).build();
        }
    }

    @Override
    public Health health() {
        if (health.getStatus() == Status.UNKNOWN) {
            checkHealth();
        }

        return this.health;
    }

    public boolean isChanged() {
        return !health.getStatus().equals(previousHealth.getStatus());
    }

    private Health getHealth() {
        var response = webhookClient.send(
                new WebhookHealthCheckNotification(UUID.randomUUID(), ZonedDateTime.now(), NotificationType.HEALTH));

        if (response.isOk()) {
            return Health.up().build();
        }

        log.trace("Webhook is down. Skipping processing pending notifications. Error details: {}", response.status());
        return Health.down().build();
    }
}
