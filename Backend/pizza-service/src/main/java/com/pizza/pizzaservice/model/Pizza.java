package com.pizza.pizzaservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "pizzas")
public class Pizza {
    @Id
    private String id;
    private String name;
    private String description;
    private double price;
    private String size; // Small, Medium, Large

    // Constructors
    public Pizza() {}

    public Pizza(String name, String description, double price, String size) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.size = size;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
