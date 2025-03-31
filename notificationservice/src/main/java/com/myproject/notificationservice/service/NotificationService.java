package com.myproject.notificationservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.myproject.common.model.PaymentResponse;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @RabbitListener(queues = "payment.queue")
    public void receivePaymentResponse(PaymentResponse response) {
        try {
            logger.info("Payment Notification received: {}", response);
            
        } catch (Exception e) {
            logger.error("Error processing payment notification: {}", e.getMessage(), e);
        }
    }
}