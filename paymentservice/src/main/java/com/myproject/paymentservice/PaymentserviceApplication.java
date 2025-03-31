package com.myproject.paymentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PaymentserviceApplication {

	public static void main(String[] args) {
		System.setProperty("management.endpoint.health.show-details", "always");
        System.setProperty("management.endpoints.web.path-mapping.health", "/admin/v1");
        System.setProperty("management.health.status.http-mapping.DOWN", "500");
        System.setProperty("management.endpoints.web.base-path", "/payment-service");

		SpringApplication.run(PaymentserviceApplication.class, args);
	}

}
