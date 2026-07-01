package com.mafasoft.notifications.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.mafasoft.notifications.other.BaseUnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.actuate.health.Status;
import org.springframework.http.HttpStatus;

public class WebhookHealthIndicatorTest extends BaseUnitTest {

    @Mock
    private WebhookClient webhookClient;

    @InjectMocks
    private WebhookHealthIndicator webhookHealthIndicator;

    @Test
    public void shouldReturnUpWhenWebhookClientRespondsWithOk() {
        // given
        givenWebhookClientResponse(HttpStatus.OK);

        // when
        webhookHealthIndicator.checkHealth();

        // then
        var health = webhookHealthIndicator.health();
        assertThat(health.getStatus()).isEqualTo(Status.UP);
    }

    @Test
    public void shouldReturnDownWhenWebhookClientRespondsWith4xx() {
        shouldReturnDownWhenWebhookClientRespondsWithStatus(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldReturnDownWhenWebhookClientRespondsWith5xx() {
        shouldReturnDownWhenWebhookClientRespondsWithStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void shouldReturnDownWhenWebhookClientRespondsWithStatus(HttpStatus httpStatus) {
        // given
        givenWebhookClientResponse(httpStatus);
        webhookHealthIndicator.checkHealth();

        // when
        var health = webhookHealthIndicator.health();

        // then
        assertThat(health.getStatus()).isEqualTo(Status.DOWN);
    }

    @Test
    public void shouldReturnTrueWhenWebhookHealthStatusChanges() {
        // when
        var isChanged = webhookHealthIndicator.isChanged();

        // then
        assertThat(isChanged).isFalse();

        // when
        givenWebhookClientResponse(HttpStatus.OK);
        webhookHealthIndicator.checkHealth();
        isChanged = webhookHealthIndicator.isChanged();

        // then
        assertThat(isChanged).isTrue();

        // when
        givenWebhookClientResponse(HttpStatus.OK);
        webhookHealthIndicator.checkHealth();
        isChanged = webhookHealthIndicator.isChanged();

        // then
        assertThat(isChanged).isFalse();

        // when
        givenWebhookClientResponse(HttpStatus.BAD_REQUEST);
        webhookHealthIndicator.checkHealth();
        isChanged = webhookHealthIndicator.isChanged();

        // then
        assertThat(isChanged).isTrue();

        // when
        givenWebhookClientResponse(HttpStatus.INTERNAL_SERVER_ERROR);
        webhookHealthIndicator.checkHealth();
        isChanged = webhookHealthIndicator.isChanged();

        // then
        assertThat(isChanged).isFalse();
    }

    private void givenWebhookClientResponse(HttpStatus httpStatus) {
        given(webhookClient.send(any())).willReturn(new WebhookResponse(httpStatus));
    }
}
