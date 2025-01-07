import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';
import {Router, RouterLink} from '@angular/router';

interface ApiResponse {
  message: string;
}
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  standalone: true,
  imports: [
    FormsModule,
    HttpClientModule
  ],
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  constructor(private http: HttpClient,private router: Router) { }


  onSubmit(data: { username: string; password: string; address: string }) {
    // Validate form data before sending it to the server
    if (data.username && data.password && data.address) {
      this.http.post<ApiResponse>('http://localhost:8080/api/register', data)
        .subscribe(
          response => {
            if (response.message=="Registered Successfully!")
            {
              alert(response.message);
              this.router.navigate(['/login']);


            }

          },
          error => console.error('Error registering ', error)
        );
    } else {
      alert('Please complete all selections before registering.');
    }
  }
}
