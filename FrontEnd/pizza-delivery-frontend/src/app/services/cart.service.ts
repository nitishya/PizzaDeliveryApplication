import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private cart: any[] = []; // Store selected pizzas
  private cartSubject = new BehaviorSubject<any[]>([]);
  private apiUrl = 'http://localhost:8083/orders'; // Order Service URL

  constructor(private http: HttpClient) {
    this.loadCartFromSessionStorage(); // Load cart from session storage when service initializes
  }

  // Load cart data from session storage
  private loadCartFromSessionStorage(): void {
    const storedCart = sessionStorage.getItem('cart');
    if (storedCart) {
      this.cart = JSON.parse(storedCart);
      this.cartSubject.next(this.cart); // Notify subscribers
    }
  }

  // Save cart data to session storage
  private saveCartToSessionStorage(): void {
    sessionStorage.setItem('cart', JSON.stringify(this.cart));
  }

  // Get cart as observable
  getCart(): Observable<any[]> {
    return this.cartSubject.asObservable();
  }

  // Add pizza to cart
  addPizzaToCart(pizza: any): void {
    this.cart.push(pizza); // Add pizza to cart
    this.saveCartToSessionStorage(); // Save updated cart to session storage
    this.cartSubject.next(this.cart); // Notify subscribers
  }

  // Remove pizza from cart
  removeFromCart(pizzaId: string): void {
    this.cart = this.cart.filter(pizza => pizza.id !== pizzaId); // Remove pizza by id
    this.saveCartToSessionStorage(); // Save updated cart to session storage
    this.cartSubject.next(this.cart); // Notify subscribers
  }

  // Get total price
  getTotalPrice(): number {
    return this.cart.reduce((total, pizza) => total + pizza.price, 0); // Calculate total price
  }

  // Clear cart
  clearCart(): void {
    this.cart = [];
    this.saveCartToSessionStorage(); // Clear cart from session storage
    this.cartSubject.next(this.cart); // Notify subscribers
  }

  // Checkout and place order
  placeOrder(userId: string): Observable<any> {
    if (this.cart.length === 0) {
      alert('Cart is empty! Please add items to your cart before placing an order.');
      return new Observable(); // Return an empty observable to prevent further execution
    }

    // Get the JWT token from session storage
    const userData = sessionStorage.getItem('user');
    if (!userData) {
      alert('You must be logged in to place an order.');
      return new Observable(); // Return an empty observable to prevent further execution
    }

    const parsedUser = JSON.parse(userData);
    const token = parsedUser.token;

    // Prepare the order data to send to the backend
    const orderData = {
      userId: userId,  // The user ID to associate the order
      pizzaIds: this.cart.map(pizza => pizza.id),  // Mapping to get pizza IDs from the cart
      totalPrice: this.getTotalPrice()  // The total price of the order
    };

    // Set up the HTTP request headers with authorization token
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });

    // Send the order data to the backend API (your backend order service URL)
    return this.http.post(this.apiUrl, orderData, { headers });
  }

  // Get cart count (number of items in the cart)
  getCartCount(): number {
    return this.cart.length; // Return total number of items in the cart
  }
}
