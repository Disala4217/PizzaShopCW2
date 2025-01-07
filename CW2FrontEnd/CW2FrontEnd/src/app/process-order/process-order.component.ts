import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Observable } from 'rxjs';

// Define a type-safe interface for pizzas
interface Pizza {
  pizza_id?: number;  // Optional ID for predefined pizzas
  name: string;
  price: number;
  crust?: string;
  sauce?: string;
  cheese?: string;
  toppings?: string[];
  message: string;
}

interface ApiResponse {
  message: string;
}

@Component({
  selector: 'app-process-order',
  templateUrl: './process-order.component.html',
  styleUrls: ['./process-order.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule]
})
export class ProcessOrderComponent implements OnInit {
  predefinedPizzas: Pizza[] = []; // Data for predefined pizzas
  crustOptions = ['Thin Crust', 'Thick Crust', 'Gluten-Free Crust'];
  sauceOptions = ['Tomato Sauce', 'BBQ Sauce'];
  cheeseOptions = ['Mozzarella Cheese', 'Cheddar Cheese', 'Parmesan Cheese'];
  toppingsOptions = ['Pepperoni', 'Mushrooms', 'Onions', 'Sausage', 'Bacon', 'Extra Cheese', 'Black Olives'];

  selectedPizza: Pizza | null = null;
  pizzaName = '';
  selectedCrust = '';
  selectedSauce = '';
  selectedCheese = '';
  selectedTopping = '';
  selectedToppings: string[] = [];
  orderPizzas: Pizza[] = [];
  showCustomizeFrame = false;
  isOrderFinalized = false;

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.fetchPredefinedPizzas();
  }

  fetchPredefinedPizzas() {
    this.getPredefinedPizzas().subscribe(
      (data) => {
        this.predefinedPizzas = data || [];
        console.log('Predefined Pizzas:', this.predefinedPizzas);
      },
      (error) => {
        console.error('Make sure that you are logged in');
        alert('Make sure that you are logged in');
      }
    );
  }

  private getPredefinedPizzas(): Observable<Pizza[]> {
    const apiUrl = 'http://localhost:8080/api/predefined';
    return this.http.get<Pizza[]>(apiUrl);
  }

  addSelectedPizzaToOrder() {
    if (this.selectedPizza) {
      this.orderPizzas.push({ ...this.selectedPizza });
      console.log('Order Pizzas:', this.orderPizzas);

      const pizzaData = {
        name: this.selectedPizza.name,
        price: this.selectedPizza.price,
      };

      this.http.post<Pizza>('http://localhost:8080/api/pizzaBuilder', pizzaData)
        .subscribe(
          response => {
            alert("Pizza Added Successfully \n" + response.message);
          },
          error => {
            alert('Error creating pizza \n' + error);
          }
        );
    } else {
      alert('Please select a valid pizza.');
    }
    this.selectedPizza = null;
  }

  toggleCustomizePizzaFrame() {
    this.showCustomizeFrame = !this.showCustomizeFrame;
  }

  addTopping() {
    if (this.selectedTopping && !this.selectedToppings.includes(this.selectedTopping)) {
      this.selectedToppings.push(this.selectedTopping);
      this.selectedTopping = '';
    }
  }

  removeTopping(index: number) {
    this.selectedToppings.splice(index, 1);
  }

  addCustomPizza() {
    if (this.pizzaName && this.selectedCrust && this.selectedSauce && this.selectedCheese) {
      this.buildPizza().then((price) => {
        const customPizza: Pizza = {
          name: this.pizzaName,
          price: price,
          crust: this.selectedCrust,
          sauce: this.selectedSauce,
          cheese: this.selectedCheese,
          toppings: [...this.selectedToppings],
          message: '',
        };
        this.orderPizzas.push(customPizza);
        this.resetCustomization();
        this.toggleCustomizePizzaFrame();
        console.log('Order Pizzas:', this.orderPizzas);
      }).catch(error => {
        alert('Failed to add pizza to order.');
      });
    } else {
      alert('Please complete all customization fields.');
    }
  }

  resetCustomization() {
    this.pizzaName = '';
    this.selectedCrust = '';
    this.selectedSauce = '';
    this.selectedCheese = '';
    this.selectedToppings = [];
  }

  removePizza(pizza: Pizza) {
    this.orderPizzas = this.orderPizzas.filter((p) => p !== pizza);
  }

  buildPizza(): Promise<number> {
    return new Promise((resolve, reject) => {
      if (this.selectedCrust && this.selectedSauce && this.selectedCheese && this.pizzaName) {
        const pizzaData = {
          name: this.pizzaName,
          crust: this.selectedCrust,
          sauce: this.selectedSauce,
          cheese: this.selectedCheese,
          toppings: this.selectedToppings,
        };

        this.http.post<Pizza>('http://localhost:8080/api/pizzaBuilder', pizzaData)
          .subscribe(
            response => {
              resolve(response.price);
            },
            error => {
              reject(error);
            }
          );
      } else {
        reject('Incomplete pizza customization');
      }
    });
  }

  calculateTotalPrice(): number {
    return this.orderPizzas.reduce((total, pizza) => total + (pizza.price || 0), 0);
  }

  finalizeOrder() {
    if (this.orderPizzas.length > 0) {
      const total = this.calculateTotalPrice();
      const orderData = {
        pizzas: this.orderPizzas.map(pizza => ({
          name: pizza.name,
          price: pizza.price,
        })),
      };

      this.http.post<any>('http://localhost:8080/api/order', orderData)
        .subscribe(
          response => {
            alert(response.message);
          },
          error => {
            alert(`Error placing the order. \n${error}`);
          }
        );

      if (confirm(`Order finalized! Total: Rs. ${total}. Press okay to continue`)) {
        this.isOrderFinalized = true;
      }
    } else {
      alert('Your order is empty!');
    }
  }

  resetOrder() {
    this.orderPizzas = [];
    this.isOrderFinalized = false;
  }

  selectDelivery() {
    alert('You have selected delivery.');
    window.open('/payment', 'PaymentWindow', 'width=800,height=600,resizable=yes,scrollbars=yes');
    this.http.post<ApiResponse>('http://localhost:8080/api/Delivery',"Delivery")
      .subscribe(
        response =>{
          alert(response.message);
        },
        error => alert('Error Delivering Order \n'+ error)
      );
    this.resetOrder();
  }

  selectPickup() {
    alert('You have selected pickup.');
    window.open('/payment', '_blank');
    this.http.post<ApiResponse>('http://localhost:8080/api/PickUP', "PickUP")
      .subscribe(
        response =>{
          alert(response.message);

        },
        error => alert('Error Picking up Order \n'+ error)
      );
    this.resetOrder();
  }

  addToFavorites() {
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
