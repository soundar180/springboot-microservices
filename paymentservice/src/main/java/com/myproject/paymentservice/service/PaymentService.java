package com.myproject.paymentservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.stereotype.Service;

import com.myproject.common.model.PaymentRequest;
import com.myproject.common.model.PaymentResponse;

@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    private final RabbitTemplate rabbitTemplate;

    private static final String PAYMENT_EXCHANGE = "payment.exchange";
    private static final String PAYMENT_ROUTING_KEY = "payment.status";

    public PaymentService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
    }

    public PaymentResponse processPayment(PaymentRequest request) {
        logger.info("Processing payment for Order ID: {}", request.getOrderId());

        PaymentResponse paymentResponse = new PaymentResponse(request.getOrderId(), "SUCCESS");

        try {
            rabbitTemplate.convertAndSend(PAYMENT_EXCHANGE, PAYMENT_ROUTING_KEY, paymentResponse);
            logger.info("Payment message sent to RabbitMQ: {}", paymentResponse);
        } catch (Exception e) {
            logger.error("Failed to send payment message to RabbitMQ", e);
            return new PaymentResponse(request.getOrderId(), "FAILED");
        }

        return paymentResponse;
    }
}
