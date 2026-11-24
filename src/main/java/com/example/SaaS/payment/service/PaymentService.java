package com.example.SaaS.payment.service;

import com.example.SaaS.common.entity.*;
import com.example.SaaS.common.exception.BusinessException;
import com.example.SaaS.company.CompanyRepository;
import com.example.SaaS.company.customer.CustomerRepository;
import com.example.SaaS.company.order.OrderRepository;
import com.example.SaaS.payment.dto.PaymentRequest;
import com.example.SaaS.payment.dto.PaymentResponse;
import com.example.SaaS.payment.repository.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;

    @Value("${stripe.secret.key:sk_test_dummy}")
    private String stripeSecretKey;

    // ========== CREATE PAYMENT (STRIPE) ==========
    @Transactional
    public PaymentResponse createStripePayment(Long customerId, Long companyId, PaymentRequest request) {

        // Validate order
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new BusinessException("Order not found"));

        if (!order.getCustomerId().getId().equals(customerId)) {
            throw new BusinessException("Order does not belong to this customer");
        }

        if (!order.getCompanyId().getId().equals(companyId)) {
            throw new BusinessException("Order does not belong to this company");
        }

        if (!"PENDING".equals(order.getStatus())) {
            throw new BusinessException("Only pending orders can be paid");
        }

        // Check if payment already exists
        paymentRepository.findByOrderId(order.getId())
                .ifPresent(p -> {
                    if ("SUCCESS".equals(p.getStatus())) {
                        throw new BusinessException("Order already paid");
                    }
                });

        Customer customer = customerRepository.getReferenceById(customerId);
        Company company = companyRepository.getReferenceById(companyId);

        // Initialize Stripe
        Stripe.apiKey = stripeSecretKey;

        try {
            // Calculate amount in cents for Stripe
            BigDecimal totalPrice = order.getOrderPrice().getSubTotal()
                    .subtract(order.getOrderPrice().getDiscount())
                    .add(order.getOrderPrice().getShippingPrice());

            long amountInCents = totalPrice.multiply(BigDecimal.valueOf(100)).longValue();

            // Create Stripe PaymentIntent
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents)
                    .setCurrency(request.getCurrency().toLowerCase())
                    .setDescription("Order #" + order.getId() + " - Company: " + company.getName())
                    .putMetadata("orderId", order.getId().toString())
                    .putMetadata("customerId", customerId.toString())
                    .putMetadata("companyId", companyId.toString())
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .build()
                    )
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            // Save payment record
            Payment payment = Payment.builder()
                    .orderId(order)
                    .customerId(customer)
                    .companyId(company)
                    .amount(totalPrice)
                    .currency(request.getCurrency())
                    .paymentMethod("STRIPE")
                    .status("PENDING")
                    .gatewayPaymentId(paymentIntent.getId())
                    .gatewayResponse(paymentIntent.toJson())
                    .build();

            Payment saved = paymentRepository.save(payment);

            return PaymentResponse.builder()
                    .id(saved.getId())
                    .orderId(order.getId())
                    .amount(saved.getAmount())
                    .currency(saved.getCurrency())
                    .paymentMethod(saved.getPaymentMethod())
                    .status(saved.getStatus())
                    .gatewayPaymentId(paymentIntent.getId())
                    .clientSecret(paymentIntent.getClientSecret())
                    .createdAt(saved.getCreatedAt())
                    .build();

        } catch (StripeException e) {
            log.error("Stripe error: {}", e.getMessage(), e);

            // Save failed payment
            Payment failedPayment = Payment.builder()
                    .orderId(order)
                    .customerId(customer)
                    .companyId(company)
                    .amount(request.getAmount())
                    .currency(request.getCurrency())
                    .paymentMethod("STRIPE")
                    .status("FAILED")
                    .errorMessage(e.getMessage())
                    .build();

            paymentRepository.save(failedPayment);

            throw new BusinessException("Payment failed: " + e.getMessage());
        }
    }

    // ========== HANDLE STRIPE WEBHOOK ==========
    @Transactional
    public void handleStripeWebhook(String paymentIntentId, String status) {
        Payment payment = paymentRepository.findByGatewayPaymentId(paymentIntentId)
                .orElseThrow(() -> new BusinessException("Payment not found"));

        switch (status) {
            case "succeeded":
                payment.setStatus("SUCCESS");
                payment.setPaidAt(Instant.now());

                // Update order status
                Order order = payment.getOrderId();
                order.setStatus("PAID");
                orderRepository.save(order);
                break;

            case "payment_failed":
                payment.setStatus("FAILED");
                payment.setErrorMessage("Payment failed via webhook");
                break;

            case "canceled":
                payment.setStatus("CANCELLED");
                break;

            default:
                payment.setStatus("PROCESSING");
        }

        paymentRepository.save(payment);
    }

    // ========== CONFIRM PAYMENT (Manual for CASH/WALLET) ==========
    @Transactional
    public PaymentResponse confirmPayment(Long paymentId, Long companyId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new BusinessException("Payment not found"));

        if (!payment.getCompanyId().getId().equals(companyId)) {
            throw new BusinessException("Payment does not belong to this company");
        }

        if (!"PENDING".equals(payment.getStatus())) {
            throw new BusinessException("Only pending payments can be confirmed");
        }

        payment.setStatus("SUCCESS");
        payment.setPaidAt(Instant.now());

        // Update order
        Order order = payment.getOrderId();
        order.setStatus("PAID");
        orderRepository.save(order);

        Payment saved = paymentRepository.save(payment);

        return buildPaymentResponse(saved);
    }

    // ========== REFUND PAYMENT ==========
    @Transactional
    public PaymentResponse refundPayment(Long paymentId, Long companyId, String reason) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new BusinessException("Payment not found"));

        if (!payment.getCompanyId().getId().equals(companyId)) {
            throw new BusinessException("Payment does not belong to this company");
        }

        if (!"SUCCESS".equals(payment.getStatus())) {
            throw new BusinessException("Only successful payments can be refunded");
        }

        if ("STRIPE".equals(payment.getPaymentMethod())) {
            Stripe.apiKey = stripeSecretKey;

            try {
                com.stripe.model.Refund refund = com.stripe.model.Refund.create(
                        com.stripe.param.RefundCreateParams.builder()
                                .setPaymentIntent(payment.getGatewayPaymentId())
                                .setReason(com.stripe.param.RefundCreateParams.Reason.REQUESTED_BY_CUSTOMER)
                                .build()
                );

                payment.setStatus("REFUNDED");
                payment.setRefundAmount(payment.getAmount());
                payment.setRefundReason(reason);
                payment.setRefundedAt(Instant.now());
                payment.setGatewayTransactionId(refund.getId());

            } catch (StripeException e) {
                log.error("Refund error: {}", e.getMessage(), e);
                throw new BusinessException("Refund failed: " + e.getMessage());
            }
        } else {
            // Manual refund
            payment.setStatus("REFUNDED");
            payment.setRefundAmount(payment.getAmount());
            payment.setRefundReason(reason);
            payment.setRefundedAt(Instant.now());
        }

        // Update order
        Order order = payment.getOrderId();
        order.setStatus("CANCELLED");
        orderRepository.save(order);

        Payment saved = paymentRepository.save(payment);
        return buildPaymentResponse(saved);
    }

    // ========== GET PAYMENT ==========
    public PaymentResponse getPayment(Long paymentId, Long customerId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new BusinessException("Payment not found"));

        if (!payment.getCustomerId().getId().equals(customerId)) {
            throw new BusinessException("Payment not found");
        }

        return buildPaymentResponse(payment);
    }

    public PaymentResponse getPaymentByOrder(Long orderId, Long customerId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new BusinessException("Payment not found for this order"));

        if (!payment.getCustomerId().getId().equals(customerId)) {
            throw new BusinessException("Payment not found");
        }

        return buildPaymentResponse(payment);
    }

    // ========== LIST PAYMENTS ==========
    public Page<PaymentResponse> getCustomerPayments(Long customerId, Pageable pageable) {
        return paymentRepository.findByCustomerId(customerId, pageable)
                .map(this::buildPaymentResponse);
    }

    public Page<PaymentResponse> getCompanyPayments(Long companyId, Pageable pageable) {
        return paymentRepository.findByCompanyId(companyId, pageable)
                .map(this::buildPaymentResponse);
    }

    public Page<PaymentResponse> getAllPayments(String status, Pageable pageable) {
        if (status != null && !status.isBlank()) {
            return paymentRepository.findByStatus(status, pageable)
                    .map(this::buildPaymentResponse);
        }
        return paymentRepository.findAll(pageable)
                .map(this::buildPaymentResponse);
    }

    // ========== HELPER ==========
    private PaymentResponse buildPaymentResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId().getId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .gatewayPaymentId(payment.getGatewayPaymentId())
                .gatewayTransactionId(payment.getGatewayTransactionId())
                .errorMessage(payment.getErrorMessage())
                .paidAt(payment.getPaidAt())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
}
