package com.mafasoft.notifications.service;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
class OAuth2AuthorizationStrategy implements WebhookAuthorizationStrategy {

    private final String tokenUrl;

    private final String clientId;

    private final String clientSecret;

    private final RestTemplate restTemplate;

    private AccessToken accessToken;

    OAuth2AuthorizationStrategy(String tokenUrl, String clientId, String clientSecret, RestTemplate restTemplate) {
        Assert.notNull(tokenUrl, "tokenUrl cannot be null");
        Assert.notNull(tokenUrl, "clientId cannot be null");
        Assert.notNull(tokenUrl, "clientSecret cannot be null");
        this.tokenUrl = tokenUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.restTemplate = restTemplate;
    }

    @Override
    public Map<String, String> getAuthorizationHeader() {
        return Map.of(HttpHeaders.AUTHORIZATION, getToken());
    }

    private String getToken() {
        if (isTokenInvalid()) {
            log.trace("Webhook access token is invalid. Getting a new one");
            synchronized (AccessToken.class) {
                if (isTokenInvalid()) {
                    getNewToken();
                }
            }
        }

        return "Bearer %s".formatted(this.accessToken.value());
    }

    private boolean isTokenInvalid() {
        return this.accessToken == null || this.accessToken.isExpired();
    }

    private void getNewToken() {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, clientSecret);

        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "client_credentials");

        var request = new HttpEntity<>(body, headers);

        var response = restTemplate.postForEntity(tokenUrl, request, Map.class).getBody();

        if (response == null) {
            throw new IllegalArgumentException("Response body should not be null");
        }

        var accessTokenString = response.get("access_token").toString();
        var expiresIn = response.get("expires_in") != null ? (Integer) response.get("expires_in") : null;

        this.accessToken = new AccessToken(accessTokenString, expiresIn);
    }
}
