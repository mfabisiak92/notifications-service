package com.mafasoft.notifications.service;

import java.util.Map;

class NoAuthorizationStrategy implements WebhookAuthorizationStrategy {

    NoAuthorizationStrategy() {}

    @Override
    public Map<String, String> getAuthorizationHeader() {
        return Map.of();
    }
}
