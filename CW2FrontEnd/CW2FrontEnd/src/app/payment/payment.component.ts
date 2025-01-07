import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { HttpClientModule } from '@angular/common/http';
import { CurrencyPipe, NgForOf, NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';

export interface PaymentDetails {
  username: string;
  address: string;
  orderDate: string;
  loyaltyPoints: number;
  totalPrice: number;
  loyaltyDiscount: number;
  christmasDiscount: number;
  newYearDiscount: number;
  finalPrice: number;
}

interface ApiResponse {
  message: string;
}

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.css'],
  imports: [CurrencyPipe, NgIf, NgForOf, HttpClientModule, FormsModule],
  standalone: true,
})
export class PaymentComponent implements OnInit {
  paymentDetails: PaymentDetails[] = [];
  showCreditCard: boolean = false;
  showDigitalWallet: boolean = false;
  showLoyaltyPoints: boolean = false;
  showCreditLoyalty: boolean = false;

  cardNumber: string | null = null;
  expiry: string | null = null;
  walletID: string | null = null;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadPaymentDetails();
  }

  private loadPaymentDetails(): void {
    const apiUrl = 'http://localhost:8080/api/payment-details';

    this.http.get<PaymentDetails[]>(apiUrl).subscribe(
      (response) => {
        this.paymentDetails = response;
        console.log(response);

      },
      (error) => {
        console.error('Error fetching payment details:', error);
        this.paymentDetails = [];
      }
    );
  }


  toggleCreditCard() {
    this.showCreditCard = true;
    this.showDigitalWallet = false;
    this.showLoyaltyPoints = false;
  }

  toggleDigitalWallet() {
    this.showDigitalWallet = true;
    this.showCreditCard = false;
    this.showLoyaltyPoints = false;
  }

  toggleLoyaltyPoints() {
    this.showLoyaltyPoints = true;
    this.showCreditCard = false;
    this.showDigitalWallet = false;

    // Show loyalty payment option if loyalty points are greater than final price

  }

  creditCardPay() {
    if (this.cardNumber && this.expiry) {
      const paymentData = {
        creditCardNumber: this.cardNumber,
        expireDate: this.expiry,
      };

      console.log('Payment data:', paymentData);

      this.http.post<ApiResponse>('http://localhost:8080/api/CreditCard', paymentData).subscribe(
        (response) => {
          alert(response.message);
          window.close();
        },
        (error) => {
          alert('Error making payment: ' + error.message);
        }
      );
    } else {
      alert('Please provide credit card details before making the payment.');
    }
  }

  DigitalWalletPay() {
    if (this.walletID) {
      const paymentData = {
        digitalWallet: this.walletID,
      };

      console.log('Payment data:', paymentData);

      this.http.post<ApiResponse>('http://localhost:8080/api/DigitalWallet', paymentData).subscribe(
        (response) => {
          alert(response.message);
          window.close();
        },
        (error) => {
          alert('Error making payment: ' + error.message);
        }
      );
    } else {
      alert('Please provide a digital wallet ID before making the payment.');
    }
  }

  loyaltyPointsPay() {
    if (this.paymentDetails.length > 0 && this.paymentDetails[0].loyaltyPoints >= this.paymentDetails[0].finalPrice) {
      const paymentData = {
        username: this.paymentDetails[0].username,
        loyaltyPointsUsed: this.paymentDetails[0].finalPrice,
      };

      console.log('Payment data:', paymentData);

      this.http.post<ApiResponse>('http://localhost:8080/api/LoyaltyPay', paymentData).subscribe(
        (response) => {
          alert(response.message);
          window.close();
        },
        (error) => {
          alert('Error making payment: ' + error.message);
        }
      );
    } else {
      alert('Insufficient loyalty points to complete the payment.');
    }
  }


}
