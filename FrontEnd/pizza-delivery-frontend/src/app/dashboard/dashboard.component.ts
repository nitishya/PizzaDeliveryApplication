import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  pizzas = [
    { name: "Pepperoni", description: "Classic pepperoni pizza", price: 12.99, image: "assets/images/pepperoni.webp" },
    { name: "FarmHouse", description: "Delicious farm fresh pizza", price: 14.99, image: "assets/images/farmhouse.webp" },
    { name: "Cheese Burst", description: "Loaded with extra cheese", price: 15.99, image: "assets/images/cheese-burst.webp" }
  ];

  userEmail: string | null = null; // Variable to hold the logged-in user's email
  userRole: string | null = null;  // To hold the user role

  constructor() {}

  ngOnInit(): void {
    this.checkUserLogin(); // Call to check if the user is logged in
  }

  // Check if the user is logged in using session storage
  checkUserLogin(): void {
    const userData = sessionStorage.getItem('user'); // Get user data from sessionStorage
    if (userData) {
      const parsedUser = JSON.parse(userData); // Parse the stored user data
      console.log('User Data:', parsedUser); // Debugging step to log user data
      this.userEmail = parsedUser.email; // Retrieve the user's email
      this.userRole = parsedUser.role;   // Retrieve the user's role
    } else {
      console.log('No user data found in sessionStorage.'); // Debugging step if no user data is found
    }
  }
}
