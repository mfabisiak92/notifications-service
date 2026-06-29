package com.mafasoft.notifications.service;

import java.util.List;
import org.springframework.util.Assert;

class NeverGiveUpRetryPolicy implements WebhookRetryPolicy {

    private final List<Integer> delays;

    NeverGiveUpRetryPolicy(List<Integer> delays) {
        Assert.notNull(delays, "delays cannot be null");
        this.delays = delays;
    }

    @Override
    public boolean shouldRetry(int attempt) {
        return true;
    }

    @Override
    public Integer getDelay(int attempt) {
        if (attempt > delays.size()) {
            return delays.get(delays.size() - 1);
        }

        return delays.get(attempt - 1);
    }
}
