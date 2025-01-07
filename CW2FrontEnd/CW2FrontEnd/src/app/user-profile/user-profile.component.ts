import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { HttpClientModule } from '@angular/common/http';
import { NgForOf } from '@angular/common';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css'],
  standalone: true, // For modern Angular, declare standalone components
  imports: [HttpClientModule, NgForOf],
})
export class UserProfileComponent implements OnInit {
  userDetail: any[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.getOrders();
  }

  getOrders(): void {
    this.http.get<any[]>('http://localhost:8080/api/getUserDetails').subscribe({
      next: (response) => {
        this.userDetail = response;
        console.log('User Details:', response);
      },
      error: (error) => {
        console.error('Error loading user details:', error);
      },
    });
  }


}
