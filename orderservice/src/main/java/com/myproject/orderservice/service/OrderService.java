package com.myproject.orderservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.myproject.common.model.PaymentRequest;
import com.myproject.common.model.PaymentResponse;
import com.myproject.orderservice.client.PaymentServiceClient;
import com.myproject.orderservice.model.Order;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class OrderService {
    private final PaymentServiceClient paymentClient;
    Logger logger = LoggerFactory.getLogger(OrderService.class);

    public OrderService(PaymentServiceClient paymentClient) {
        this.paymentClient = paymentClient;
    }

    @CircuitBreaker(name = "paymentService", fallbackMethod = "paymentFallback")
    public PaymentResponse placeOrder(Order order) {
        if (order == null || order.getId() == null || order.getPrice() <= 0 || order.getQuantity() <= 0) {
            logger.warn("Invalid order received: {}", order);
            throw new IllegalArgumentException("Invalid order details");
        }

        PaymentRequest paymentRequest = new PaymentRequest(order.getId(), order.getPrice() * order.getQuantity());
        logger.info("Processing payment request for order ID: {}", order.getId());

        try {
            PaymentResponse paymentResponse = paymentClient.processPayment(paymentRequest);
            logger.info("Payment successful for order ID: {}", order.getId());
            return paymentResponse;
        } catch (Exception e) {
            logger.error("Payment failed for order ID {}: {}", order.getId(), e.getMessage());
            return new PaymentResponse(order.getId(), "FAILED");
        }
    }

    public PaymentResponse paymentFallback(Order order, Throwable ex) {
        logger.error("Payment service is unavailable. Activating fallback for order ID: {}", order.getId());
        return new PaymentResponse(order.getId(), "PENDING");
    }
}
