import { Component } from '@angular/core';
import {NgForOf, NgIf} from '@angular/common';
import {Observable} from 'rxjs';
import {HttpClient} from "@angular/common/http";
import {HttpClientModule} from "@angular/common/http";

@Component({
  selector: 'app-feedback',
  imports: [
    NgIf,
    NgForOf,HttpClientModule
  ],
  templateUrl: './feedback.component.html',
  styleUrl: './feedback.component.css'
})
export class FeedbackComponent {
  orders: any[] = [];
  pizza:any[]=[];
  currentView = 'orders';
  username = 'sampleUser'; // Replace this with dynamic logic as necessary

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.getOrders();
    this.getPizza();
  }

  // Fetch orders directly using HttpClient
  getOrders() {
    this.http.get<any[]>(`http://localhost:8080/api/getOrders`).subscribe({
      next: (response) => {
        this.orders = response;
      },
      error: (error) => {
        console.error('Error loading orders:', error);
      },
    });
  }
  getPizza() {
    this.http.get<any[]>(`http://localhost:8080/api/getPizza`).subscribe({
      next: (response) => {
        this.pizza = response;
        console.log(response);
      },
      error: (error) => {
        console.error('Error loading orders:', error);
      },
    });
  }

  // Handle rate order button click
  rateOrder(order: any, event: Event) {
    event.stopPropagation();
    console.log('Rate order clicked for:', order);
  }
  showRatingModal: number | null = null; // Holds the order ID of the modal being shown
  selectedRating = 0; // The current selected rating

  // Open the rating modal for a specific order
  openRatingModal(orderId: number) {
    this.showRatingModal = orderId;
    this.selectedRating = 0; // Reset rating
  }

  // Close the modal
  closeModal() {
    this.showRatingModal = null;
  }

  // Select a rating
  selectRating(star: number) {
    this.selectedRating = star;
  }

  submitRating(orderId: number) {
    if (this.selectedRating > 0) { // Ensure a valid rating is selected
      console.log(`Order #${orderId} rated ${this.selectedRating} stars.`);

      // Create the data object for the POST request
      const data = {
        orderId: orderId,
        ratings: this.selectedRating
      };

      // Send POST request to the backend
      this.http.post('http://localhost:8080/api/rateOrder', data)
          .subscribe(
              response => {
                alert("Rating updated successfully");
                this.closeModal(); // Close the modal after success
              },
              error => {
                console.error('Error updating rating', error);
                alert('Failed to update rating. Please try again.');
              }
          );
    } else {
      alert('Please select a valid rating before submitting.');
    }
  }

  submitPizzaRating(orderId: number) {
    if (this.selectedRating > 0) { // Ensure a valid rating is selected
      console.log(`Order #${orderId} rated ${this.selectedRating} stars.`);

      // Create the data object for the POST request
      const data = {
        pizzaID: orderId,
        ratings: this.selectedRating
      };
      console.log(data);

      // Send POST request to the backend
      this.http.post('http://localhost:8080/api/ratePizza', data)
          .subscribe(
              response => {
                alert("Rating updated successfully");
                this.closeModal(); // Close the modal after success
              },
              error => {
                console.error('Error updating rating', error);
                alert('Failed to update rating. Please try again.');
              }
          );
    } else {
      alert('Please select a valid rating before submitting.');
    }
  }


  CurrentView: string = 'orders'; // Default view
  selectedOrder: { id: number; totalPrice: number; pizzas: { id: number; name: string; price: number }[] } | null = null;




  switchView(view: string) {
    this.currentView = view;
  }

  selectOrder(order: typeof this.orders[0]) {
    this.selectedOrder = order;
    this.switchView('pizzas');
  }




  ratePizza(pizza: { id: number; name: string; price: number }, event: Event) {
    event.stopPropagation();
    const rating = prompt(`Rate ${pizza.name} (1-5):`);
    if (rating && this.isValidRating(rating)) {
      alert(`${pizza.name} rated ${rating} stars!`);
    } else {
      alert('Invalid rating. Please enter a number between 1 and 5.');
    }
  }

  backToOrders() {
    this.switchView('orders');
    this.selectedOrder = null;
  }

  private isValidRating(rating: string): boolean {
    const numberRating = parseInt(rating, 10);
    return !isNaN(numberRating) && numberRating >= 1 && numberRating <= 5;
  }
}







