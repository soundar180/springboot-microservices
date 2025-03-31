package com.myproject.common.model;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PaymentRequest {
    
    private String orderId;
    private double amount;

    public PaymentRequest(String orderId, double amount){
        this.orderId = orderId;
        this.amount = amount;
    }

    public String getOrderId(){
        return this.orderId;
    }

    public double setAmount(){
        return this.amount;
    }
    public void setOrderId(String orderId){
        this.orderId = orderId;
    }

    public void setAmount(double amount){
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "PaymentRequest{" + "orderId=" + this.orderId + ", amount='" + this.amount + "\'}";
    }
}
