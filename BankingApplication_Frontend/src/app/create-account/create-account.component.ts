import { Component, OnInit } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { AuthService } from '../Services/auth.service';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';

import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-create-account',
  standalone: true,
  imports: [FormsModule,CommonModule,HttpClientModule],
  templateUrl: './create-account.component.html',
  styleUrl: './create-account.component.css'
})

export class CreateAccountComponent implements OnInit {
  selectedRole!: string;

  constructor(private router:Router,private route: ActivatedRoute,private authService: AuthService) {}

  ngOnInit() {
    // Retrieve the 'role' query parameter from the URL
    this.route.queryParams.subscribe(params => {
      this.selectedRole = params['role'];
    });
  }

  username: string = '';
  password: string = '';
  email: string = '';
  confirmPassword: string = '';


  passwordVisible: boolean = false;  // Flag to toggle visibility of password
  confirmPasswordVisible: boolean = false;  // Flag for confirm password visibility

  togglePasswordVisibility() {
    this.passwordVisible = !this.passwordVisible;
  }

  toggleConfirmPasswordVisibility() {
    this.confirmPasswordVisible = !this.confirmPasswordVisible;
  }

  createAccount(accountForm: NgForm) {
    console.log(this.password);
    console.log(this.password !== this.confirmPassword);
    
    if (this.password !== this.confirmPassword) {
      alert('Passwords do not match!');
      return;
    }

    const user = { username: this.username, email: this.email, password: this.password,confirmPassword:this.confirmPassword,role:'Customer' };
    console.log(user)

    this.authService.registerUser(user).subscribe({
      next: (response) => {
        // console.log(response);
        if (response.status === 'success') {  
          alert(response.message);
          this.router.navigate(['/'], { queryParams: { role: 'Customer' } });
        }
        
      },
      error: (error) => {
        console.log(error);
        
        alert('Error: ' + error.error.message);
      },
      complete: () => {
        console.log('Request complete');
      }
    });
    
  }

  UserloginAccount() {
    console.log('Guest creating account');
    this.router.navigate(['/'], { queryParams: { role: this.selectedRole } });//login button
  }
  
}


