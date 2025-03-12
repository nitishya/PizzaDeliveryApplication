package com.pizza.pizzaservice.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pizza.pizzaservice.exception.PizzaNotFoundException;
import com.pizza.pizzaservice.model.Pizza;
import com.pizza.pizzaservice.repository.PizzaRepository;

@Service
public class PizzaService {

	private static final Logger logger = LoggerFactory.getLogger(PizzaService.class);

	@Autowired
	private PizzaRepository pizzaRepository;

	public List<Pizza> getAllPizzas() {
		logger.info("Fetching all pizzas");
		return pizzaRepository.findAll();
	}

	public Pizza getPizzaById(String id) {
		logger.info("Fetching pizza with ID: {}", id);
		return pizzaRepository.findById(id)
				.orElseThrow(() -> new PizzaNotFoundException("Pizza not found with ID: " + id));
	}

	public Pizza addPizza(Pizza pizza) {
		logger.info("Adding new pizza: {}", pizza.getName());
		return pizzaRepository.save(pizza);
	}

	public Pizza updatePizza(String id, Pizza updatedPizza) {
		logger.info("Updating pizza with ID: {}", id);
		if (!pizzaRepository.existsById(id)) {
			throw new PizzaNotFoundException("Pizza not found with ID: " + id);
		}
		updatedPizza.setId(id);
		return pizzaRepository.save(updatedPizza);
	}

	public void deletePizza(String id) {
		logger.info("Deleting pizza with ID: {}", id);
		if (!pizzaRepository.existsById(id)) {
			throw new PizzaNotFoundException("Pizza not found with ID: " + id);
		}
		pizzaRepository.deleteById(id);
	}
}
