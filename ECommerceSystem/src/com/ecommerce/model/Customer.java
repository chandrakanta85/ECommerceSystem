package com.ecommerce.model;

import java.io.Serializable;

public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    private int customerId;
    private String name;
    private String email;
    private String phoneNumber;

    public Customer(int customerId, String name, String email, String phoneNumber) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    // Getters
    public int getCustomerId() {
    	return customerId;
    }
    public String getName() {
    	return name; 
    }
    public String getEmail() {
    	 return email;
    }
    
    public String getphoneNumber() {
    	return phoneNumber;
    }

    @Override
    public String toString() {
        return "Customer ID: " + customerId + ", Name: " + name + ", Email: " + email + ", phoneNumber: " + phoneNumber;
    }
} 

