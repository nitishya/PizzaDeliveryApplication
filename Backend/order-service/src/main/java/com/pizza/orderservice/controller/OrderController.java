package com.pizza.orderservice.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pizza.orderservice.exception.OrderNotFoundException;
import com.pizza.orderservice.model.Order;
import com.pizza.orderservice.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {
	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private OrderService orderService;

	@GetMapping
	public List<Order> getAllOrders() {
		logger.info("Fetching all orders");
		return orderService.getAllOrders();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
		logger.info("Fetching order with ID: {}", id);
		Order order = orderService.getOrderById(id)
				.orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + id));
		return ResponseEntity.ok(order);
	}

	@GetMapping("/user/{userId}")
	public List<Order> getOrdersByUserId(@PathVariable String userId) {
		logger.info("Fetching orders for user ID: {}", userId);
		return orderService.getOrdersByUserId(userId);
	}

	@PostMapping
	public Order createOrder(@RequestBody Order order) {
		logger.info("Creating new order for user: {}", order.getUserId());
		return orderService.createOrder(order);
	}

	@PutMapping("/{id}/status")
	public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
		logger.info("Updating status of order ID: {} to {}", id, status);
		Order updatedOrder = orderService.updateOrderStatus(id, status);
		return ResponseEntity.ok(updatedOrder);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
		logger.info("Deleting order with ID: {}", id);
		orderService.deleteOrder(id);
		return ResponseEntity.noContent().build();
	}
}
