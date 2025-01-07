import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module'; // Import this
import { AppComponent } from './app.component';
import { WelcomeComponent } from './welcome/welcome.component';
import { LoginComponent } from './login/login.component';
import {RegisterComponent} from './register/register.component';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import {PizzaBuilderComponent}from './pizza-builder/pizza-builder.component';
import { CommonModule } from '@angular/common';
import {ProcessOrderComponent} from './process-order/process-order.component';
import {PaymentComponent} from './payment/payment.component';
import {FeedbackComponent} from './feedback/feedback.component';
import {UserProfileComponent} from './user-profile/user-profile.component';

@NgModule({
  declarations: [



  ],
  imports: [
    BrowserModule,
    AppRoutingModule, // Include routing
    CardModule,
    ButtonModule,
    FormsModule,
    HttpClientModule,
    WelcomeComponent, // Add the components to be declared
    LoginComponent,
    RegisterComponent,
    CommonModule,
    PizzaBuilderComponent,
    ProcessOrderComponent,
    PaymentComponent,
    FeedbackComponent,
    UserProfileComponent,

  ],
  providers: [],
  bootstrap:[]
})
export class AppModule {}
