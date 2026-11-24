package com.example.SaaS.payment.controller;

import com.example.SaaS.payment.service.PaymentService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhooks")
@RequiredArgsConstructor
@Slf4j
public class WebhookController {

    private final PaymentService paymentService;

    @Value("${stripe.webhook.secret:whsec_dummy}")
    private String webhookSecret;

    @PostMapping("/stripe")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader
    ) {
        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            log.error("Webhook signature verification failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Invalid signature");
        }

        log.info("Received Stripe webhook: {}", event.getType());

        // Handle the event
        switch (event.getType()) {
            case "payment_intent.succeeded":
                PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
                        .getObject()
                        .orElse(null);

                if (paymentIntent != null) {
                    log.info("PaymentIntent succeeded: {}", paymentIntent.getId());
                    paymentService.handleStripeWebhook(paymentIntent.getId(), "succeeded");
                }
                break;

            case "payment_intent.payment_failed":
                PaymentIntent failedIntent = (PaymentIntent) event.getDataObjectDeserializer()
                        .getObject()
                        .orElse(null);

                if (failedIntent != null) {
                    log.info("PaymentIntent failed: {}", failedIntent.getId());
                    paymentService.handleStripeWebhook(failedIntent.getId(), "payment_failed");
                }
                break;

            case "payment_intent.canceled":
                PaymentIntent canceledIntent = (PaymentIntent) event.getDataObjectDeserializer()
                        .getObject()
                        .orElse(null);

                if (canceledIntent != null) {
                    log.info("PaymentIntent canceled: {}", canceledIntent.getId());
                    paymentService.handleStripeWebhook(canceledIntent.getId(), "canceled");
                }
                break;

            default:
                log.info("Unhandled event type: {}", event.getType());
        }

        return ResponseEntity.ok("Webhook received");
    }
}
