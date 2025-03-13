import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private apiUrl = 'http://localhost:8083/orders';

  constructor(private http: HttpClient) {}

  // Fetch all orders
  getOrders(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl, { headers: this.getHeaders() });
  }

  // Fetch a single order by ID
  getOrderById(orderId: number): Observable<any> {
    const url = `${this.apiUrl}/${orderId}`;
    return this.http.get<any>(url, { headers: this.getHeaders() });
  }
  
  // Update order status (for Admins)
  updateOrderStatus(orderId: number, newStatus: string): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${orderId}/status?status=${newStatus}`, {}, { headers: this.getHeaders() });
  }

  // Helper function to get JWT headers
  private getHeaders(): HttpHeaders {
    const userData = sessionStorage.getItem('user'); // Retrieve user data

    if (!userData) {
      console.error('No user data found in session storage.');
      return new HttpHeaders(); // Return empty headers
    }

    try {
      const parsedUser = JSON.parse(userData); // Parse user object
      if (!parsedUser.token) {
        console.error('No authentication token found.');
        return new HttpHeaders();
      }

      return new HttpHeaders({ 'Authorization': `Bearer ${parsedUser.token}` });
    } catch (error) {
      console.error('Error parsing user data:', error);
      return new HttpHeaders();
    }
  }
}
