package com.project.shiphub.service.webhook;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;

public interface WebhookService {

    void processEvent(Event event);

    void handlePaymentSucceeded(Event event);

    void handlePaymentFailed(Event event);

    void handlePaymentCanceled(Event event);

    void handleRefund(Event event);

    Event constructEventFromPayload(String payload, String signature) throws SignatureVerificationException;
}