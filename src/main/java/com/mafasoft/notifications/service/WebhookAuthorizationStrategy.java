package com.mafasoft.notifications.service;

import java.util.Map;

interface WebhookAuthorizationStrategy {

    Map<String, String> getAuthorizationHeader();
}
