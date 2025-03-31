package com.myproject.notificationservice;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.myproject.common.model.PaymentResponse;
import com.myproject.notificationservice.service.NotificationService;

class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testReceivePaymentResponse() {
        PaymentResponse response = new PaymentResponse("1", "SUCCESS");
        notificationService.receivePaymentResponse(response);
    }
}
