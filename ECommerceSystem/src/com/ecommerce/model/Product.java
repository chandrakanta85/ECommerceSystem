package com.ecommerce.model;

import java.io.Serializable;


public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String description;
    private double price;
    private int stock;
    private String category;

    public Product(int id, String name, String description, double price, int stock, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }

    // Getters
    public int getId() {
    	return id;
    }
    
    public String getName() {
    	return name; 
    }
    
    public String getDescription() {
    	 return description;
    }
    
    public double getPrice() {
    	return price;
    }
    public int getStock() {
    	return stock;
    }
    
    public String getCategory() {
    	return category;
    }

    // Setters
    public void setName(String name) {
    	 this.name = name; 
    }
    
    public void setDescription(String description) {
    	this.description = description;
    }
    
    public void setPrice(double price) {
    	 this.price = price; 
    }
    
    public void setStock(int stock) {
    	this.stock = stock; 
    }
    
    public void setCategory(String category) {
    	this.category = category;
    }

    @Override
    public String toString() {
        return String.format("Product ID: %d | Name: %s | Price: $%.2f | Stock: %d | Category: %s",
                id, name, price, stock, category);
    }
}
