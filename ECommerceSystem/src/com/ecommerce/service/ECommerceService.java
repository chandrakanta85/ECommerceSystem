package com.ecommerce.service;

import com.ecommerce.model.Customer;
import com.ecommerce.model.Order;
import com.ecommerce.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ECommerceService {
    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/system_db";
    private static final String USER = "root"; // <-- CHANGE THIS
    private static final String PASSWORD = "Rakesh@123"; // <-- CHANGE THIS

    private Connection connection;

    public ECommerceService() {
        try {
            // Establish the database connection
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to the database successfully.");
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            // Exit the application if we can't connect to the database
            System.exit(1);
        }
    }

    // --- Product Management ---
    public void addProduct(Product product) {
        String sql = "INSERT INTO products (id, name, description, price, stock, category) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, product.getId());
            pstmt.setString(2, product.getName());
            pstmt.setString(3, product.getDescription());
            pstmt.setDouble(4, product.getPrice());
            pstmt.setInt(5, product.getStock());
            pstmt.setString(6, product.getCategory());
            pstmt.executeUpdate();
            System.out.println("Product added successfully: " + product.getName());
        } catch (SQLException e) {
            System.err.println("Error adding product: " + e.getMessage());
        }
    }

    public Optional<Product> findProductById(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getInt("stock"),
                    rs.getString("category")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error finding product: " + e.getMessage());
        }
        return Optional.empty();
    }
    
    public boolean updateProduct(int id, String newName, String newDesc, double newPrice, int newStock, String newCategory) {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, stock = ?, category = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setString(2, newDesc);
            pstmt.setDouble(3, newPrice);
            pstmt.setInt(4, newStock);
            pstmt.setString(5, newCategory);
            pstmt.setInt(6, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product updated successfully.");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error updating product: " + e.getMessage());
        }
        System.out.println("Error: Product with ID " + id + " not found.");
        return false;
    }

    public boolean removeProduct(int id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product removed successfully.");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error removing product: " + e.getMessage());
        }
        System.out.println("Error: Product with ID " + id + " not found.");
        return false;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                products.add(new Product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getInt("stock"),
                    rs.getString("category")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all products: " + e.getMessage());
        }
        return products;
    }

    // --- Customer Management ---
    public void addCustomer(Customer customer) {
        String sql = "INSERT INTO customers (customerId, name, email,phoneNumber) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, customer.getCustomerId());
            pstmt.setString(2, customer.getName());
            pstmt.setString(3, customer.getEmail());
            pstmt.setString(4, customer.getphoneNumber());
            pstmt.executeUpdate();
            System.out.println("Customer registered: " + customer.getName());
        } catch (SQLException e) {
        	//check for MySql duplicate entry
        	if(e.getErrorCode() == 1062) {
        		System.err.println("Error: A customer with this email or phoneNumber is already register.");
        	} else {
        		System.err.println("Error adding customer: " + e.getMessage());
        	}  
        }
    }

    public Optional<Customer> findCustomerById(int id) {
        String sql = "SELECT * FROM customers WHERE customerId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Customer(
                    rs.getInt("customerId"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phoneNumber")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error finding customer: " + e.getMessage());
        }
        return Optional.empty();
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                customers.add(new Customer(
                    rs.getInt("customerId"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phoneNumber")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all customers: " + e.getMessage());
        }
        return customers;
    }

    // --- Order Management ---
    public void placeOrder(Order order) {
        String sql = "INSERT INTO orders (orderId, customerId, totalCost, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, order.getOrderId());
            pstmt.setInt(2, order.getCustomer().getCustomerId());
            pstmt.setDouble(3, order.getTotalCost());
            pstmt.setString(4, order.getStatus());
            pstmt.executeUpdate();
            System.out.println("Order placed successfully. Order ID: " + order.getOrderId());
        } catch (SQLException e) {
            System.err.println("Error placing order: " + e.getMessage());
        }
    }

    public Optional<Order> findOrderById(int id) {
        String sql = "SELECT * FROM orders WHERE orderId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Optional<Customer> customer = findCustomerById(rs.getInt("customerId"));
                if (customer.isPresent()) {
                    return Optional.of(new Order(
                        rs.getInt("orderId"),
                        customer.get(),
                        new ArrayList<>(), // Products list is empty in this version
                        rs.getDouble("totalCost")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding order: " + e.getMessage());
        }
        return Optional.empty();
    }
    
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Optional<Customer> customer = findCustomerById(rs.getInt("customerId"));
                customer.ifPresent(c -> {
					try {
						orders.add(new Order(
						    rs.getInt("orderId"),
						    c,
						    new ArrayList<>(),
						    rs.getDouble("totalCost")
						));
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
            }
        } catch (SQLException e) {
            System.err.println("Error getting all orders: " + e.getMessage());
        }
        return orders;
    }
}
