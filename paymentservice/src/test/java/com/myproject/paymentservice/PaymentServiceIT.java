package com.myproject.paymentservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.myproject.common.model.PaymentRequest;
import com.myproject.common.model.PaymentResponse;
import com.myproject.paymentservice.service.PaymentService;



@SpringBootTest
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PaymentServiceIT {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceIT.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private PaymentService paymentService;

    private static final String PAYMENT_EXCHANGE = "payment.exchange";
    private static final String PAYMENT_ROUTING_KEY = "payment.status";

    private static final String PAYMENT_QUEUE = "payment.queue";

    
    @Container
    @ServiceConnection
    public static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer(DockerImageName.parse("rabbitmq:3.7.25-management-alpine"));

    @BeforeAll
    static void startContainer() {
        rabbitMQContainer.start();
    }

    @BeforeEach
    void setupQueue() {
        // Create queue before test runs
        rabbitTemplate.execute(channel -> {
            channel.queueDeclare(PAYMENT_QUEUE, true, false, false, null);
            channel.queueBind(PAYMENT_QUEUE, PAYMENT_EXCHANGE, PAYMENT_ROUTING_KEY);
            return null;
        });
    }
    
    @Test
    void testRabbitMQConnection() {
        assertTrue(rabbitMQContainer.isRunning());
    }

    @Test
    void testEndToEndPaymentProcessAndNotification() throws InterruptedException {
        // Given an order
        PaymentRequest paymentRequest = new PaymentRequest("order-123", 100.5);
        
        // When process order
        PaymentResponse paymentResponse = paymentService.processPayment(paymentRequest);

        // Then payment should be successful
        assertThat(paymentResponse).isNotNull();
        assertThat(paymentResponse.getOrderId()).isEqualTo(paymentRequest.getOrderId());
        assertThat(paymentResponse.getStatus()).isEqualTo("SUCCESS");

        // Retrieve message from RMQ 
        PaymentResponse receivedMessage = null;
        for (int i = 0; i < 5; i++) {
            receivedMessage = (PaymentResponse) rabbitTemplate.receiveAndConvert(PAYMENT_QUEUE);
            if (receivedMessage != null) {
                break;
            }
            logger.info("Retry: {} receivedMessage: {}", i, receivedMessage);
            TimeUnit.SECONDS.sleep(1); // Wait 1 second before retrying
        }


        // Validate Notification Received
        assertThat(receivedMessage).isNotNull();
        assertThat(receivedMessage.getOrderId()).isEqualTo(paymentRequest.getOrderId());
        assertThat(receivedMessage.getStatus()).isEqualTo("SUCCESS");
    }
}
