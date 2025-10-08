package com.ecommerce.main;

import com.ecommerce.model.Customer;
import com.ecommerce.model.Order;
import com.ecommerce.model.Product;
import com.ecommerce.service.ECommerceService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static final ECommerceService service = new ECommerceService();
    private static final Scanner scanner = new Scanner(System.in);
    private static int orderIdCounter = 1;
    private static int customerIdCounter = 1;

    public static void main(String[] args) {
        int choice;
        do {
            printMenu();
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); 
                handleChoice(choice);
            } catch (java.util.InputMismatchException e) {
                System.err.println("Invalid input. Please enter a number.");
                scanner.nextLine(); 
                choice = 0; 
            }
        } while (choice != 7);
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n--- E-Commerce System Menu ---");
        System.out.println("1. Add a Product");
        System.out.println("2. Update a Product");
        System.out.println("3. Remove a Product");
        System.out.println("4. Register a Customer");
        System.out.println("5. Place an Order");
        System.out.println("6. View All Products & Orders");
        System.out.println("7. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void handleChoice(int choice) {
        switch (choice) {
            case 1:
                addProduct();
                break;
            case 2:
                updateProduct();
                break;
            case 3:
                removeProduct();
                break;
            case 4:
                registerCustomer();
                break;
            case 5:
                placeOrder();
                break;
            case 6:
                viewAllProductsAndOrders();
                break;
            case 7:
                System.out.println("Exiting application. Data will be saved automatically.");
                break;
            default:
                System.err.println("Invalid choice. Please enter a number from the menu.");
                break;
        }
    }

    private static void addProduct() {
        try {
            System.out.print("Enter Product ID: ");
            int id = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter Product Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter Description: ");
            String description = scanner.nextLine();
            System.out.print("Enter Price: ");
            double price = scanner.nextDouble();
            System.out.print("Enter Stock Quantity: ");
            int stock = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter Category: ");
            String category = scanner.nextLine();
            service.addProduct(new Product(id, name, description, price, stock, category));
        } catch (java.util.InputMismatchException e) {
            System.err.println("Invalid input. Please ensure ID, Price, and Stock are numbers.");
            scanner.nextLine();
        }
    }
    
    private static void updateProduct() {
        try {
            System.out.print("Enter Product ID to update: ");
            int id = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter new Name: ");
            String newName = scanner.nextLine();
            System.out.print("Enter new Description: ");
            String newDesc = scanner.nextLine();
            System.out.print("Enter new Price: ");
            double newPrice = scanner.nextDouble();
            System.out.print("Enter new Stock: ");
            int newStock = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter new Category: ");
            String newCategory = scanner.nextLine();
            service.updateProduct(id, newName, newDesc, newPrice, newStock, newCategory);
        } catch (java.util.InputMismatchException e) {
            System.err.println("Invalid input. Please ensure ID, Price, and Stock are numbers.");
            scanner.nextLine();
        }
    }

    private static void removeProduct() {
        try {
            System.out.print("Enter Product ID to remove: ");
            int id = scanner.nextInt();
            scanner.nextLine();
            service.removeProduct(id);
        } catch (java.util.InputMismatchException e) {
            System.err.println("Invalid input. Product ID must be a number.");
            scanner.nextLine();
        }
    }

    private static void registerCustomer() {
        System.out.print("Enter Customer Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Customer Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Customer Phonenumber: ");
        String phoneNumber = scanner.nextLine();
        service.addCustomer(new Customer(customerIdCounter++, name, email, phoneNumber));
    }

    private static void placeOrder() {
        System.out.print("Enter Customer ID for the order: ");
        int customerId = scanner.nextInt();
        scanner.nextLine();
        Optional<Customer> customerOpt = service.findCustomerById(customerId);

        if (customerOpt.isEmpty()) {
            System.err.println("Error: Customer not found. Please register the customer first.");
            return;
        }

        List<Product> productsInOrder = new ArrayList<>();
        double totalCost = 0.0;
        String continueOrdering = "yes";

        while (continueOrdering.equalsIgnoreCase("yes")) {
            System.out.print("Enter Product ID to add to order: ");
            try {
                int productId = scanner.nextInt();
                scanner.nextLine();
                Optional<Product> productOpt = service.findProductById(productId);

                if (productOpt.isPresent()) {
                    Product p = productOpt.get();
                    if (p.getStock() > 0) {
                        productsInOrder.add(p);
                        p.setStock(p.getStock() - 1); 
                        totalCost += p.getPrice();
                        System.out.println("Product added to order: " + p.getName());
                    } else {
                        System.err.println("Product is out of stock.");
                    }
                } else {
                    System.err.println("Product not found.");
                }
            } catch (java.util.InputMismatchException e) {
                System.err.println("Invalid input. Please enter a number for the product ID.");
                scanner.nextLine();
            }

            System.out.print("Add another product? (yes/no): ");
            continueOrdering = scanner.nextLine();
        }

        if (!productsInOrder.isEmpty()) {
            Order newOrder = new Order(orderIdCounter++, customerOpt.get(), productsInOrder, totalCost);
            service.placeOrder(newOrder);
        } else {
            System.out.println("No products were added. Order cancelled.");
        }
    }

    private static void viewAllProductsAndOrders() {
        System.out.println("\n--- All Products ---");
        List<Product> products = service.getAllProducts();
        if (products.isEmpty()) {
            System.out.println("No products available.");
        } else {
            products.forEach(System.out::println);
        }
        
        System.out.println("\n--- All Customers ---");
        List<Customer> customers = service.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("No customers registered.");
        } else {
            customers.forEach(System.out::println);
        }

        System.out.println("\n--- All Orders ---");
        List<Order> orders = service.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("No orders placed yet.");
        } else {
            orders.forEach(System.out::println);
        }
    }
}

