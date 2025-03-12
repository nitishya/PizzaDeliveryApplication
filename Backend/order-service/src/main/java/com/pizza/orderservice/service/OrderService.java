package com.pizza.orderservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pizza.orderservice.exception.OrderNotFoundException;
import com.pizza.orderservice.model.Order;
import com.pizza.orderservice.repository.OrderRepository;

@Service
public class OrderService {
	private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

	@Autowired
	private OrderRepository orderRepository;

	/**
	 * Retrieves all orders.
	 */
	public List<Order> getAllOrders() {
		logger.info("Fetching all orders");
		return orderRepository.findAll();
	}

	/**
	 * Retrieves an order by its ID, throws exception if not found.
	 */
	public Optional<Order> getOrderById(Long id) {
		logger.info("Fetching order with ID: {}", id);
		return Optional.ofNullable(orderRepository.findById(id)
				.orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + id)));
	}

	/**
	 * Retrieves orders by user ID.
	 */
	public List<Order> getOrdersByUserId(String userId) {
		logger.info("Fetching orders for user ID: {}", userId);
		return orderRepository.findByUserId(userId);
	}

	/**
	 * Creates a new order with default status "PENDING" and sets order time.
	 */
	public Order createOrder(Order order) {
		logger.info("Creating order for user: {}", order.getUserId());
		order.setStatus("PENDING");
		order.setOrderTime(LocalDateTime.now()); // Ensure timestamp is set
		return orderRepository.save(order);
	}

	/**
	 * Updates order status, throws exception if order not found.
	 */
	public Order updateOrderStatus(Long id, String status) {
		logger.info("Updating order status for ID: {} to {}", id, status);
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + id));
		order.setStatus(status);
		return orderRepository.save(order);
	}

	/**
	 * Deletes an order by ID, throws exception if order not found.
	 */
	public void deleteOrder(Long id) {
		logger.info("Deleting order with ID: {}", id);
		if (!orderRepository.existsById(id)) {
			throw new OrderNotFoundException("Order not found with ID: " + id);
		}
		orderRepository.deleteById(id);
	}
}
