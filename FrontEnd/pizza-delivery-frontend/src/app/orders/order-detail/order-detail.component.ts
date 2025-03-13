import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { OrderService } from '../../services/order.service';

@Component({
  selector: 'app-order-detail',
  templateUrl: './order-detail.component.html',
  styleUrls: ['./order-detail.component.css']
})
export class OrderDetailComponent implements OnInit {
  order: any; // Stores order details
  isAdmin: boolean = false; // Check if user is admin
  statuses = ['PENDING', 'CONFIRMED', 'DELIVERED']; // Status options

  constructor(
    private route: ActivatedRoute,
    private orderService: OrderService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const orderId = String(this.route.snapshot.paramMap.get('id')); // Convert to string
    if (orderId) {
      this.fetchOrder(orderId);
    }

    // Check if user is an Admin
    const userRole = localStorage.getItem('role');
    this.isAdmin = userRole === 'ADMIN';
}



  fetchOrder(orderId: string): void {
    const orderIdNumber = Number(orderId); // Convert to number
    this.orderService.getOrderById(orderIdNumber).subscribe({
      next: (response) => {
        this.order = response;
      },
      error: (error) => {
        console.error('Error fetching order:', error);
      }
    });
  }

  updateOrderStatus(newStatus: string): void {
    if (!this.order || !this.isAdmin) return;

    this.orderService.updateOrderStatus(this.order.id, newStatus).subscribe({
      next: (updatedOrder) => {
        this.order.status = updatedOrder.status; // Update status on UI
        console.log('Order status updated:', updatedOrder);
      },
      error: (error) => {
        console.error('Error updating order status:', error);
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/orders']);
  }
}
