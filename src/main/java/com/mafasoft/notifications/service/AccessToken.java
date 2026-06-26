package com.mafasoft.notifications.service;

import java.time.Instant;

class AccessToken {

    private static final int TOKEN_EXPIRATION_THRESHOLD_IN_SECONDS = 30;

    private final String value;
    private final Instant expiresAt;

    AccessToken(String value, Integer expiresInSeconds) {
        this.value = value;
        this.expiresAt = expiresInSeconds != null ? Instant.now().plusSeconds(expiresInSeconds) : null;
    }

    boolean isExpired() {
        if (expiresAt == null) {
            return false;
        }

        return !Instant.now().plusSeconds(TOKEN_EXPIRATION_THRESHOLD_IN_SECONDS).isBefore(expiresAt);
    }

    String value() {
        return value;
    }
}
