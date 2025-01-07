import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';
import { SessionService } from '../services/session.service';
import {RouterLink} from '@angular/router';

interface ApiResponse {
  message: string;
  ID?: string;
  username?: string;
  address?: string;
  FavPizza?: string;
  Loyelty?: string;
}

@Component({
  selector: 'login-component',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  standalone: true,
  imports: [
    FormsModule,
    HttpClientModule,
    RouterLink,
  ]
})
export class LoginComponent {
  constructor(private http: HttpClient, private sessionService: SessionService) {}

  onSubmit(data: { username: string; password: string }) {
    if (data.username && data.password) {
      this.http.post<ApiResponse>('http://localhost:8080/api/login', data)
        .subscribe(
          response => {
            // Save properties to session storage
            if (response.ID) sessionStorage.setItem('ID', response.ID);
            if (response.username) sessionStorage.setItem('username', response.username);
            if (response.address) sessionStorage.setItem('address', response.address);
            if (response.FavPizza) sessionStorage.setItem('FavPizza', response.FavPizza);
            if (response.Loyelty) sessionStorage.setItem('Loyelty', response.Loyelty);

            // Set session flags
            sessionStorage.setItem('isLoggedIn', 'true');

            alert("Logged in successfully \n welcome " + response.username);
            window.location.href = '/**'; // Replace with the URL to redirect after login
          },
          error => {
            alert('Username or password is wrong');
          }
        );
    } else {
      alert('Please enter both username and password.');
    }
  }
}
