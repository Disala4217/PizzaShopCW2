// app-routing.module.ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { WelcomeComponent } from './welcome/welcome.component';
import { LoginComponent } from './login/login.component';
import {RegisterComponent} from './register/register.component';
import {PizzaBuilderComponent} from './pizza-builder/pizza-builder.component';
import {ProcessOrderComponent} from './process-order/process-order.component';
import {PaymentComponent} from './payment/payment.component';
import {FeedbackComponent} from './feedback/feedback.component';
import {UserProfileComponent} from './user-profile/user-profile.component';

export const routes: Routes = [
  { path: '', component: WelcomeComponent },
  { path: '', redirectTo: '/welcome', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  {path:'register',component:RegisterComponent},
  {path:'pizza-builder',component:PizzaBuilderComponent},
  {path:'process-order',component:ProcessOrderComponent},
  {path:'payment',component:PaymentComponent},
  {path:'feedback',component:FeedbackComponent},
  {path:'user-profile',component:UserProfileComponent},
  { path: '**', redirectTo: '' } // Wildcard route for handling 404
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutes {}
