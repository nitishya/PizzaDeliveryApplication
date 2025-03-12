package com.pizza.orderservice.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String userId;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime orderTime;

	private double totalPrice;
	private String status; // PENDING, CONFIRMED, DELIVERED

	@ElementCollection
	private List<String> pizzaIds;

	// Constructors
	public Order() {
	}

	public Order(String userId, List<String> pizzaIds, double totalPrice, String status) {
		this.userId = userId;
		this.pizzaIds = pizzaIds;
		this.totalPrice = totalPrice;
		this.status = status;
		this.orderTime = LocalDateTime.now();
	}

	// Ensure orderTime is set before persisting to DB
	@PrePersist
	protected void onCreate() {
		if (this.orderTime == null) {
			this.orderTime = LocalDateTime.now();
		}
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public LocalDateTime getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(LocalDateTime orderTime) {
		this.orderTime = orderTime;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<String> getPizzaIds() {
		return pizzaIds;
	}

	public void setPizzaIds(List<String> pizzaIds) {
		this.pizzaIds = pizzaIds;
	}
}
