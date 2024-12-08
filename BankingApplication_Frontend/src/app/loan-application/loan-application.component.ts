import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-loan-application',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './loan-application.component.html',
  styleUrl: './loan-application.component.css'
})
export class LoanApplicationComponent {
  loanDetails = history.state.loan;
  loanAmount: number = 0;
  selectedFile: File | null = null;

  onFileSelect(event: any) {
    this.selectedFile = event.target.files[0];
  }

  submitLoanApplication() {
    console.log('Loan Details:', this.loanDetails);
    console.log('Selected File:', this.selectedFile);
    // Logic to send the file to the backend can be added here
  }

}
