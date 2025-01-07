import { Component } from '@angular/core';
import { Router } from '@angular/router';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import {NgForOf, NgIf} from '@angular/common';

@Component({
  selector: 'welcome-component',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css'],
  imports: [HttpClientModule, NgForOf]

})
export class WelcomeComponent {
  pizza:any[]=[];

  constructor(private http: HttpClient) {}
  ngOnInit() {
    this.getPizza();
  }

  // Fetch orders directly using HttpClient

  getPizza() {
    this.http.get<any[]>(`http://localhost:8080/api/getTopPizza`).subscribe({
      next: (response) => {
        this.pizza = response;
        console.log(response);
      },
      error: (error) => {
        console.error('Error loading orders:', error);
      },
    });
  }


}
