package com.myproject.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.myproject.common.model.PaymentRequest;
import com.myproject.common.model.PaymentResponse;

@FeignClient(name = "payment-service", url = "http://payment-service:12302/payment-service/api/v1")
public interface PaymentServiceClient {
    @PostMapping("process-payment")
    PaymentResponse processPayment(@RequestBody PaymentRequest request);
}