package com.myproject.paymentservice;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import com.myproject.common.model.PaymentRequest;
import com.myproject.common.model.PaymentResponse;
import com.myproject.paymentservice.service.PaymentService;

import static org.junit.jupiter.api.Assertions.*;

class PaymentServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), any(PaymentResponse.class));
        doNothing().when(rabbitTemplate).setMessageConverter(any(Jackson2JsonMessageConverter.class));
    }

    @Test
    void testProcessPayment_Success() {
        
        PaymentRequest request = new PaymentRequest("123", 100.0);
        PaymentResponse expectedResponse = new PaymentResponse("123", "SUCCESS");

        
        PaymentResponse actualResponse = paymentService.processPayment(request);

        
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getOrderId(), actualResponse.getOrderId());
        assertEquals(expectedResponse.getStatus(), actualResponse.getStatus());

        
        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), any(PaymentResponse.class));
    }
}
