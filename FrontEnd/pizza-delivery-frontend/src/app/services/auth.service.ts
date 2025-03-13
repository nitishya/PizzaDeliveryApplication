import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private loginUrl = 'http://localhost:8080/auth/login';  
  private registerUrl = 'http://localhost:8081/auth/register'; 

  constructor(private http: HttpClient) {}

  // Login method
  login(email: string, password: string): Observable<any> {
    return this.http.post<any>(this.loginUrl, { email, password });
  }

  // Register method
  register(userData: any): Observable<any> {
    return this.http.post<any>(this.registerUrl, userData);
  }

  // Store user data in **Session Storage** instead of Local Storage
  storeUserData(token: string, user: any): void {
    sessionStorage.setItem('token', token);  // Store token in session
    sessionStorage.setItem('user', JSON.stringify(user));  // Store user details
  }

  // Check if user is logged in
  isLoggedIn(): boolean {
    const token = sessionStorage.getItem('token');
    return !!token; // Returns true if token exists
  }

  // Retrieve user data
  getUser(): any {
    const user = sessionStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  }

  // Logout method
  logout(): void {
    sessionStorage.removeItem('token'); // Remove token
    sessionStorage.removeItem('user');  // Remove user data
  }
}
