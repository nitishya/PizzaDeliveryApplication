import { Component, OnInit } from '@angular/core';
import { PizzaService } from '../../services/pizza.service';
import { CartService } from '../../services/cart.service';

@Component({
  selector: 'app-pizza-list',
  templateUrl: './pizza-list.component.html',
  styleUrls: ['./pizza-list.component.css']
})
export class PizzaListComponent implements OnInit {
  pizzas: any[] = []; // Store pizza data

  constructor(private pizzaService: PizzaService, private cartService: CartService) {}

  ngOnInit(): void {
    this.fetchPizzas(); // Fetch pizzas on component load
  }

  // Fetch pizza data with authentication headers
  fetchPizzas(): void {
    this.pizzaService.getPizzas().subscribe({
      next: (data) => {
        this.pizzas = data; // Assign fetched pizzas to the pizzas array
        console.log('Pizzas fetched:', data);
      },
      error: (err) => console.error('Error fetching pizzas:', err)
    });
  }

  // Add the selected pizza to the cart
  addToCart(pizza: any): void {
    this.cartService.addPizzaToCart(pizza); // Call CartService to add pizza
    alert(`✅ ${pizza.name} added to cart!`); // Display confirmation to user
  }

  // Get the current count of pizzas in the cart
  getCartCount(): number {
    return this.cartService.getCartCount(); // Fetch the current cart count
  }
}
