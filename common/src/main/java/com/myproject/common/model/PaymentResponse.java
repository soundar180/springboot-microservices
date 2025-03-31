package com.myproject.common.model;
import java.io.Serializable;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PaymentResponse implements Serializable{
    private String orderId;
    private String status;
    
    public PaymentResponse(String orderId, String status){
        this.orderId = orderId;
        this.status = status;
    }

    public String getOrderId(){
        return this.orderId;
    }

    public String getStatus(){
        return this.status;
    }

    @Override
    public String toString() {
        return "PaymentResponse{" + "orderId=" + this.orderId + ", status='" + this.status + "\'}";
    }
}
