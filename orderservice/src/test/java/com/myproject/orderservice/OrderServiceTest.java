package com.myproject.orderservice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.myproject.common.model.PaymentRequest;
import com.myproject.common.model.PaymentResponse;
import com.myproject.orderservice.client.PaymentServiceClient;
import com.myproject.orderservice.model.Order;
import com.myproject.orderservice.service.OrderService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private PaymentServiceClient paymentClient;

    @InjectMocks
    private OrderService orderService;

    private Order validOrder;

    @BeforeEach
    void setUp() {
        validOrder = new Order();
        validOrder.setId("ORDER123");
        validOrder.setPrice(100);
        validOrder.setQuantity(2);
    }

    @Test
    void testPlaceOrder_Success() {
    
        PaymentResponse mockResponse = new PaymentResponse("ORDER123", "SUCCESS");
        when(paymentClient.processPayment(any(PaymentRequest.class))).thenReturn(mockResponse);

        PaymentResponse response = orderService.placeOrder(validOrder);

        assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        verify(paymentClient, times(1)).processPayment(any(PaymentRequest.class));
    }

    @Test
    void testPlaceOrder_InvalidOrder_ThrowsException() {
        // Missing ID, Price, and Quantity
        Order invalidOrder = new Order();  

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.placeOrder(invalidOrder);
        });

        assertEquals("Invalid order details", exception.getMessage());
    }

    @Test
    void testPlaceOrder_PaymentFailure_ReturnsFailedResponse() {
        
        when(paymentClient.processPayment(any(PaymentRequest.class)))
                .thenThrow(new RuntimeException("Payment Service Down"));

        PaymentResponse response = orderService.placeOrder(validOrder);

        assertNotNull(response);
        assertEquals("FAILED", response.getStatus());
    }

    @Test
    void testPaymentFallback_TriggeredOnFailure() {
        PaymentResponse fallbackResponse = orderService.paymentFallback(validOrder, new RuntimeException("Service Down"));

        assertNotNull(fallbackResponse);
        assertEquals("PENDING", fallbackResponse.getStatus());
    }
}
