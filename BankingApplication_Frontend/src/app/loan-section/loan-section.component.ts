import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';
import { CommonModule, NgFor, NgIf } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-loan-section',
  standalone: true,
  imports: [FormsModule, NgIf, NgFor, CommonModule, RouterModule],
  templateUrl: './loan-section.component.html',
  styleUrls: ['./loan-section.component.css']
})
export class LoanSectionComponent implements OnInit {
  loanTypes = [
    { type: 'PERSONAL', interestRate: 7.5, duration: 5, image: '/assets/personal_loan.jpg' },
    { type: 'HOME', interestRate: 6.2, duration: 20, image: '/assets/home_loan.jpg' },
    { type: 'VECHILE', interestRate: 8.0, duration: 7, image: '/assets/vechile_loan.jpg' }
  ];
  
  loanApplicationFormVisible: boolean = false;
  selectedLoan: any;
  userId: string | null = null;  // To store the userId from URL
  loanFormData = {
    loanAmount: null,
    loanPurpose: '',
    loanType: '',
    documentUrl: ''  // To hold the URL of the uploaded document
  };

  constructor(private http: HttpClient, private activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    // Get the userId from the route parameters
    this.activatedRoute.queryParams.subscribe(params => {
      this.userId = params['userId']; // Get userId from the URL query params
      console.log('User ID:', this.userId);
    });

  }

  // Open loan application form with the selected loan type
  openLoanApplicationForm(loan: any) {
    this.selectedLoan = loan;
    this.loanFormData.loanType = loan.type;  // Set the loan type in the form
    this.loanApplicationFormVisible = true;
  }

  // Close loan application form
  closeLoanForm() {
    this.loanApplicationFormVisible = false;
    this.resetLoanForm();
  }

  // Reset loan application form
  resetLoanForm() {
    this.loanFormData = { loanAmount: null, loanPurpose: '', loanType: '', documentUrl: '' };
  }

  // Submit loan application form
  submitLoanApplication() {
    // Check if required fields are filled
    if (this.loanFormData.loanAmount === null || this.loanFormData.loanAmount === undefined) {
      alert('Please enter a valid loan amount.');
      return;
    }
    
    
    if (!this.loanFormData.documentUrl) {
      alert('Please provide the document URL.');
      return;
    }

    if (!this.userId) {
      alert('User ID is missing.');
      return;
    }

    // Prepare the API request body
    const body = {
      userId: this.userId,
      loanAmount: this.loanFormData.loanAmount,
      interestRate: this.selectedLoan.interestRate, // Add the interest rate from selected loan type
      loanType: this.loanFormData.loanType, // This will be in uppercase (e.g., "PERSONAL")
      documentUrl: this.loanFormData.documentUrl // The URL provided by the user
    };

    // Make the API call
    this.http.post('http://localhost:8092/loans/apply', body)
      .pipe(
        catchError(error => {
          console.error('Error submitting loan application:', error);
          alert('There was an error submitting your application. Please try again later.');
          return of(null); // return an observable to handle the error gracefully
        })
      )
      .subscribe(response => {
        if (response) {
          console.log('Loan Application Submitted:', response);
          alert('Your loan application has been submitted successfully.');
          this.closeLoanForm(); // Close form after submission
        }
      });
  }
}
