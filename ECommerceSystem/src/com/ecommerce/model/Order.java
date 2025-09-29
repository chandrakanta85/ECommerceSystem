package com.ecommerce.model;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    private int orderId;
    private Customer customer;
    private List<Product> products;
    private double totalCost;
    private String status;

    public Order(int orderId, Customer customer, List<Product> products, double totalCost) {
        this.orderId = orderId;
        this.customer = customer;
        this.products = products;
        this.totalCost = totalCost;
        this.status = "Pending";
    }

    // Getters and Setters
    public int getOrderId() {
    	return orderId;
    }
    public Customer getCustomer() {
    	return customer; 
    }
    public List<Product> getProducts(){
    	return products; 
    }
    public double getTotalCost() {
    	return totalCost;
    }
    public String getStatus() {
    	 return status; 
    }
    public void setStatus(String status) {
    	this.status = status;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Order ID: %d | Customer: %s | Status: %s | Total: $%.2f\n",
                orderId, customer.getName(), status, totalCost));
        sb.append("Products:\n");
        for (Product p : products) {
            sb.append("  - ").append(p.getName()).append(" ($").append(p.getPrice()).append(")\n");
        }
        return sb.toString();
    }
}
