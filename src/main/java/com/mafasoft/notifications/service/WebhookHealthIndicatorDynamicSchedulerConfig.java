package com.mafasoft.notifications.service;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.PeriodicTrigger;

@Slf4j
class WebhookHealthIndicatorDynamicSchedulerConfig implements SchedulingConfigurer {

    private final WebhookHealthIndicator webhookHealthIndicator;

    private final PeriodicTrigger defaultTrigger;

    private final PeriodicTrigger decreasedTrigger;

    WebhookHealthIndicatorDynamicSchedulerConfig(
            WebhookHealthIndicator webhookHealthIndicator,
            int defaultIntervalInSeconds,
            int decreasedIntervalInSeconds) {
        this.webhookHealthIndicator = webhookHealthIndicator;
        this.defaultTrigger = new PeriodicTrigger(Duration.of(defaultIntervalInSeconds, ChronoUnit.SECONDS));
        this.decreasedTrigger = new PeriodicTrigger(Duration.of(decreasedIntervalInSeconds, ChronoUnit.SECONDS));
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(webhookHealthIndicator::checkHealth, triggerContext -> {
            var health = webhookHealthIndicator.health();
            var trigger = getTrigger(health);

            if (webhookHealthIndicator.isChanged()) {
                log.info(
                        "Webhook is {}. Changing interval for webhook health check to {} seconds",
                        health.getStatus(),
                        trigger.getPeriodDuration().toSeconds());
            }

            return trigger.nextExecution(triggerContext);
        });
    }

    private PeriodicTrigger getTrigger(Health health) {
        if (health.getStatus() == Status.DOWN) {
            return decreasedTrigger;
        }

        return defaultTrigger;
    }
}
