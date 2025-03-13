import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class PizzaService {
  private apiUrl = 'http://localhost:8082/pizzas'; // Pizza API URL

  constructor(private http: HttpClient) {}

  // Get auth token from sessionStorage with validation
  private getAuthHeaders(): HttpHeaders {
    const userData = sessionStorage.getItem('user'); // Retrieve user object

    if (!userData) {
      console.error('⚠️ No user data found in sessionStorage.');
      return new HttpHeaders(); // Return empty headers
    }

    try {
      const parsedUser = JSON.parse(userData); // Parse user object
      if (!parsedUser.token) {
        console.error('🚨 No authentication token found.');
        return new HttpHeaders();
      }

      return new HttpHeaders({
        'Authorization': `Bearer ${parsedUser.token}`,
        'Content-Type': 'application/json'
      });
    } catch (error) {
      console.error('❌ Error parsing user data:', error);
      return new HttpHeaders();
    }
  }

  // Fetch all pizzas (Authenticated)
  getPizzas(): Observable<any[]> {
    const headers = this.getAuthHeaders();
    
    if (!headers.has('Authorization')) {
      console.error('❌ Authorization header is missing.');
      return throwError(() => new Error('User not authenticated. Please log in again.'));
    }

    return this.http.get<any[]>(this.apiUrl, { headers })
      .pipe(
        catchError((error) => {
          console.error('❌ Error fetching pizzas:', error);
          return throwError(() => new Error('Failed to fetch pizzas. Please try again.'));
        })
      );
  }
}
