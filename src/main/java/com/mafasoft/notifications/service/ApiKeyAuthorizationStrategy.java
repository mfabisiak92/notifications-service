package com.mafasoft.notifications.service;

import java.util.Map;
import org.springframework.util.Assert;

class ApiKeyAuthorizationStrategy implements WebhookAuthorizationStrategy {

    private final String headerName;

    private final String apiKey;

    ApiKeyAuthorizationStrategy(String headerName, String apiKey) {
        Assert.notNull(headerName, "headerName cannot be null");
        Assert.notNull(apiKey, "apiKey cannot be null");
        this.headerName = headerName;
        this.apiKey = apiKey;
    }

    @Override
    public Map<String, String> getAuthorizationHeader() {
        return Map.of(headerName, apiKey);
    }
}
