package com.mafasoft.notifications.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public record WebhookResponse(HttpStatusCode status, String error) {

    WebhookResponse(HttpStatusCode status) {
        this(status, null);
    }

    boolean isOk() {
        return HttpStatus.OK == status;
    }
}
