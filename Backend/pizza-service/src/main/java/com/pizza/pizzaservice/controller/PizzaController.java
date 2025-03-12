package com.pizza.pizzaservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pizza.pizzaservice.model.Pizza;
import com.pizza.pizzaservice.service.PizzaService;

@RestController
@RequestMapping("/pizzas")
public class PizzaController {

    @Autowired
    private PizzaService pizzaService;

    // Fetch all pizzas
    @GetMapping
    public List<Pizza> getAllPizzas() {
        return pizzaService.getAllPizzas();
    }

    // Fetch a pizza by ID
    @GetMapping("/{id}")
    public ResponseEntity<Pizza> getPizzaById(@PathVariable String id) {
        // Since PizzaService already handles the exception throwing, no need to check here.
        Pizza pizza = pizzaService.getPizzaById(id);
        return ResponseEntity.ok(pizza);
    }

    // Add a new pizza
    @PostMapping
    public Pizza addPizza(@RequestBody Pizza pizza) {
        return pizzaService.addPizza(pizza);
    }

    // Update a pizza by ID
    @PutMapping("/{id}")
    public ResponseEntity<Pizza> updatePizza(@PathVariable String id, @RequestBody Pizza updatedPizza) {
        Pizza pizza = pizzaService.updatePizza(id, updatedPizza);
        return pizza != null ? ResponseEntity.ok(pizza) : ResponseEntity.notFound().build();
    }

    // Delete a pizza by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePizza(@PathVariable String id) {
        pizzaService.deletePizza(id);
        return ResponseEntity.noContent().build();
    }
}
