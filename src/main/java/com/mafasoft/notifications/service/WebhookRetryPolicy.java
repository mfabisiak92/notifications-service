package com.mafasoft.notifications.service;

interface WebhookRetryPolicy {

    boolean shouldRetry(int attempt);

    Integer getDelay(int attempt);
}
