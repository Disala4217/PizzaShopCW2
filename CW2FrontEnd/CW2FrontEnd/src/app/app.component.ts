import { Component } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: true, // Ensure this is specified if using standalone components
  imports: [
    CommonModule,       // Import CommonModule for directives like *ngIf
    RouterLink,         // Enables router links
    RouterOutlet        // Enables route rendering
  ],
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'CW2FrontEnd';

  constructor() {}

  logout() {
    sessionStorage.removeItem('isLoggedIn');
    window.location.href = '/login'; // Redirect after logout
  }

  isLoggedIn(): boolean {
    return sessionStorage.getItem('isLoggedIn') === 'true';
  }
}
