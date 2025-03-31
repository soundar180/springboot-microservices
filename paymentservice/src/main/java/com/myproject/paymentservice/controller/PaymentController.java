package com.myproject.paymentservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.myproject.common.model.PaymentRequest;
import com.myproject.common.model.PaymentResponse;
import com.myproject.paymentservice.service.PaymentService;;

@RestController
@RequestMapping("/payment-service")
public class PaymentController {

    Logger logger = LoggerFactory.getLogger(PaymentController.class);
    @Autowired
    private PaymentService paymentService;
    
    @PostMapping("api/v1/process-payment")
    public PaymentResponse processPayment(@RequestBody PaymentRequest request) {

        logger.info("request: {}", request);
        return paymentService.processPayment(request);

    }
}