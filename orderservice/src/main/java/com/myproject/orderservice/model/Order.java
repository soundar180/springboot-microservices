package com.myproject.orderservice.model;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Order {
    private String id;
    private String product;
    private int quantity;
    private double price;

    public Order(String id, String product, int quantity, double price) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    public String getId(){
        return id;
    }

    public String getProduct(){
        return product;
    }

    public int getQuantity(){
        return quantity;
    }

    public double getPrice(){
        return price;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setProduct(String product){
        this.product = product;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public void setPrice(double price){
        this.price = price;
    }

    @Override
    public String toString() {
        return "Order{" + "id=" + this.id + ", product='" + this.product + '\'' + ", quantity='" + this.quantity + '\'' + ", price='" + this.price + '\''  + '}';
    }
}
