package com.mafasoft.notifications.service;

import java.nio.charset.Charset;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Assert;

class BasicAuthorizationStrategy implements WebhookAuthorizationStrategy {

    private final String username;

    private final String password;

    BasicAuthorizationStrategy(String username, String password) {
        Assert.notNull(username, "username cannot be null");
        Assert.notNull(password, "password cannot be null");
        this.username = username;
        this.password = password;
    }

    public Map<String, String> getAuthorizationHeader() {
        return Map.of(HttpHeaders.AUTHORIZATION, authorizationHeaderValue());
    }

    private String authorizationHeaderValue() {
        return "Basic %s".formatted(HttpHeaders.encodeBasicAuth(username, password, Charset.defaultCharset()));
    }
}
