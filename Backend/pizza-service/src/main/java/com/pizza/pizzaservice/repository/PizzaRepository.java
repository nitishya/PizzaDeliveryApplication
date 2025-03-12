package com.pizza.pizzaservice.repository;

import com.pizza.pizzaservice.model.Pizza;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PizzaRepository extends MongoRepository<Pizza, String> {
    // Additional query methods (if needed) can be added here
}

