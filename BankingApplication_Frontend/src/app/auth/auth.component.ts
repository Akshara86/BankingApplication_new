import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';  // ActivatedRoute to read query params
import { AuthService } from '../Services/auth.service';

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [FormsModule, CommonModule, HttpClientModule],
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.css']
})
export class AuthComponent implements OnInit {
  selectedRole: string = '';  
  username: string = '';
  email: string = '';
  password: string = '';

  constructor(
    private router: Router,
    private authService: AuthService,
    private route: ActivatedRoute  // Inject ActivatedRoute to access query params
  ) {}

  ngOnInit(): void {
    // Get 'role' query parameter if available
    this.route.queryParams.subscribe(params => {
      this.selectedRole = params['role'] || '';  // Set 'selectedRole' to query param 'role'
    });
  }

  login() {
    if (this.selectedRole === 'Admin' || this.selectedRole === 'Customer') {
      const user = { email: this.email, password: this.password, role: this.selectedRole };

      this.authService.loginUser(user).subscribe({
        next: (response) => {
          if (response.status === 'success') {  // Check for 'success' status in the response
            if(this.selectedRole === 'Customer'){
              this.router.navigate(['/user'],{ queryParams: { userId: response.data.id } });
            }else if (this.selectedRole === 'Admin'){
              this.router.navigate(['/admin'],{ queryParams: { userId: response.data.id } })
            }
          } else {
            alert('Invalid credentials!');
          }
        },
        error: (error) => {
          console.log(error);
          alert('Error: ' + error.error.message);
        }
      });
    }
  }

  viewResources() {
    console.log('Guest viewing resources');
    this.router.navigate(['/view-resources']);
  }

  GuestcreateAccount() {
    console.log('Guest creating account');
    this.router.navigate(['/create-account'], { queryParams: { role: this.selectedRole } });
  }
}
