import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  errorMessage: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  onLogin() {
    this.authService.login(this.email, this.password).subscribe(
      (response) => {
        console.log('Login successful', response);

        // Store full user data including ID in sessionStorage
        const userData = {
          id: response.id,
          email: response.email,
          role: response.role,
          token: response.token
        };

        sessionStorage.setItem('user', JSON.stringify(userData));
        sessionStorage.setItem('token', response.token);

        // Navigate to dashboard
        this.router.navigate(['/dashboard']);
      },
      (error) => {
        console.error('Login failed', error);
        this.errorMessage = 'Invalid email or password!';
      }
    );
  }
}
