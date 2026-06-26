package com.mafasoft.notifications.service;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
class WebhookClient {

    private final String webhookUrl;
    private final RestTemplate restTemplate;
    private final WebhookAuthorizationStrategy webhookAuthorizationStrategy;

    private static final Map<String, String> headersToSend = Map.of(
            HttpHeaders.ACCEPT, "*/*",
            HttpHeaders.CONTENT_TYPE, "application/json",
            HttpHeaders.USER_AGENT, "MAFASOFT-WebhookClient");

    WebhookResponse send(Object content) {
        log.trace("Sending request {} to {}", content, webhookUrl);

        try {
            var httpHeaders = new HttpHeaders();
            webhookAuthorizationStrategy.getAuthorizationHeader().forEach(httpHeaders::set);
            headersToSend.forEach(httpHeaders::set);
            var requestEntity = new HttpEntity<>(content, httpHeaders);
            log.trace("Sending request {} to {}", content, webhookUrl);
            ResponseEntity<Object> responseEntity = restTemplate.postForEntity(webhookUrl, requestEntity, Object.class);
            log.trace("Sent request {} to {}. Response: {}", content, webhookUrl, responseEntity);
            return new WebhookResponse(responseEntity.getStatusCode());
        } catch (RestClientResponseException ex) {
            log.trace(
                    "Received error for request {} sent to {}. Status code: {}. Error message: {}",
                    content,
                    webhookUrl,
                    ex.getStatusCode(),
                    ex.getMessage());
            return new WebhookResponse(ex.getStatusCode(), ex.getMessage());
        }
    }
}
