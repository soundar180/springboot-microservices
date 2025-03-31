package com.myproject.orderservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myproject.common.model.PaymentResponse;
import com.myproject.orderservice.model.*;
import com.myproject.orderservice.service.OrderService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;


@RestController
@RequestMapping("order-service")
public class OrderServiceController {
    
    Logger logger = LoggerFactory.getLogger(OrderServiceController.class);

    @Autowired
    private OrderService orderService;
    
    @PostMapping("api/v1/orders")
    @CircuitBreaker(name = "orderService", fallbackMethod = "fallbackOrder")
    @RateLimiter(name = "orderService")
    public ResponseEntity<PaymentResponse> createOrder(@RequestBody Order order) {
        
        logger.info(" New order Request {}", order);

        PaymentResponse response = orderService.placeOrder(order);
        return ResponseEntity.ok(response);
    
    }

    public ResponseEntity<String> fallbackOrder(Order order, Throwable t) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Order Service is overloaded, please try again later.");
    }
    
}
