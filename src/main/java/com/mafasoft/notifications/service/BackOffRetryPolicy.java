package com.mafasoft.notifications.service;

import java.util.List;
import org.springframework.util.Assert;

class BackOffRetryPolicy implements WebhookRetryPolicy {

    private final List<Integer> delays;

    BackOffRetryPolicy(List<Integer> delays) {
        Assert.notNull(delays, "delays cannot be null");
        this.delays = delays;
    }

    @Override
    public boolean shouldRetry(int attempt) {
        return delays.size() >= attempt;
    }

    @Override
    public Integer getDelay(int attempt) {
        return delays.get(attempt - 1);
    }
}
