import { Component, OnInit } from '@angular/core';
import { CartService } from '../../services/cart.service';
import { OrderService } from '../../services/order.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-order-list',
  templateUrl: './order-list.component.html',
  styleUrls: ['./order-list.component.css']
})
export class OrderListComponent implements OnInit {
  orders: any[] = [];
  cartItems: any[] = [];
  totalPrice: number = 0;

  constructor(
    private cartService: CartService,
    private orderService: OrderService,
    private router: Router
  ) {}

  ngOnInit() {
    // Fetch cart data
    this.cartItems = JSON.parse(sessionStorage.getItem('cart') || '[]'); // Use session storage
    this.totalPrice = this.cartService.getTotalPrice(); // Get total price from CartService

    const userData = sessionStorage.getItem('user'); // Get user data from session storage
    if (!userData) {
      alert('You must be logged in to place an order');
      this.router.navigate(['/login']); // Redirect to login page
      return;
    }

    const parsedUser = JSON.parse(userData);
    const userId = parsedUser.id; // Extract user ID
    const token = parsedUser.token; // Extract token

    if (!userId || !token) {
      alert('User authentication failed! Please log in again.');
      this.router.navigate(['/login']);
      return;
    }

    // Fetch existing orders and filter them based on the logged-in user ID
    this.orderService.getOrders().subscribe({
      next: (data) => {
        console.log('Received Orders:', data);

        // Filter orders to only show those for the logged-in user
        this.orders = data.filter(order => order.userId === userId);

        if (this.orders.length === 0) {
          alert('No orders found for this user.');
        }
      },
      error: (error) => {
        console.error('Error fetching orders:', error);
        if (error.status === 401) {
          alert('Session expired. Please log in again.');
          this.router.navigate(['/login']);
        }
      }
    });
  }

  // Place an order
  placeOrder() {
    if (this.cartItems.length === 0) {
      alert('Your cart is empty! Please add items before placing an order.');
      return;
    }

    const userData = sessionStorage.getItem('user');
    if (!userData) {
      alert('User authentication failed! Please log in again.');
      this.router.navigate(['/login']);
      return;
    }

    const parsedUser = JSON.parse(userData);
    const userId = parsedUser.id;

    // Use CartService to place the order
    this.cartService.placeOrder(userId).subscribe({
      next: (response) => {
        alert('Order placed successfully!');
        this.cartService.clearCart(); // Clear the cart after successful order
        this.router.navigate(['/order-history']); // Navigate to order history
      },
      error: (error) => {
        console.error('Error placing order:', error);
        alert('Error placing order. Please try again later.');
      }
    });
  }

  // Remove pizza from cart
  removeFromCart(pizzaId: string) {
    this.cartService.removeFromCart(pizzaId);

    // Update the cartItems and total price from CartService after removal
    this.cartItems = JSON.parse(sessionStorage.getItem('cart') || '[]');
    this.totalPrice = this.cartService.getTotalPrice();
  }
}
