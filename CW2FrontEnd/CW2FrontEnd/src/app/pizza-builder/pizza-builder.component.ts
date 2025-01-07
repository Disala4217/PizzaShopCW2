import {Component, OnInit} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';

interface ApiResponse {
  message: string;
}
@Component({
  selector: 'app-pizza-builder',
  templateUrl: './pizza-builder.component.html',
  styleUrls: ['./pizza-builder.component.css'],
  standalone: true,
  imports: [HttpClientModule, FormsModule, CommonModule],
})

export class PizzaBuilderComponent {
  crustOptions = ['Thin Crust', 'Thick Crust', 'Gluten-Free Crust'];
  sauceOptions = ['Tomato Sauce', 'BBQ Sauce'];
  cheeseOptions = ['Mozzarella Cheese', 'Cheddar Cheese', 'Parmesan Cheese'];
  toppingsOptions = ['Pepperoni', 'Mushrooms', 'Onions', 'Sausage','Bacon','Extra Cheese','Black Olives'];

  selectedCrust: string | undefined;
  selectedSauce: string | undefined;
  selectedCheese: string | undefined;
  selectedTopping: string | undefined;
  selectedToppings: string[] = [];
  pizzaName: string = '';

  constructor(private http: HttpClient) {}

  addTopping() {
    if (this.selectedTopping) {
      this.selectedToppings.push(this.selectedTopping);
      this.selectedTopping = undefined;
    }
  }

  removeTopping(index: number) {
    this.selectedToppings.splice(index, 1);
  }



  AddToFavorites() {
    if (this.selectedCrust && this.selectedSauce && this.selectedCheese && this.pizzaName) {
      const pizzaData = {
        name: this.pizzaName,
        crust: this.selectedCrust,
        sauce: this.selectedSauce,
        cheese: this.selectedCheese,
        toppings: this.selectedToppings,
      };

      console.log('Pizza Data:', pizzaData);

      this.http.post<ApiResponse>('http://localhost:8080/api/FavPizza', pizzaData)
        .subscribe(
          response =>{
            alert(response.message);
          },
          error => alert('Error creating pizza \n'+ error)
        );
    } else {
      alert('Please complete all selections before saving the pizza.');
    }

  }


}
