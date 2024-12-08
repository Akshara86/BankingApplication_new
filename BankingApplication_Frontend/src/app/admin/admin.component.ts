import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AdminService } from '../Services/admin.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, FormsModule],  // Make sure CommonModule is added here
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
  // Sidebar state
  isSidebarOpen = true;

  // Dashboard values
  totalUsers: string | null = null;
  activeUsers: string | null = null;

  // Transactions
  transactions: any[] = [];

  // Loans
  loans: any[] = [];
  isLoanLoading = false;

  // Loader
  isLoading = false;

  // State to manage the view (dashboard, transactions, loans)
  showDashboardSection = true;
  showTransactionsSection = false;
  showLoansSection = false;

  constructor(private adminService: AdminService, private router: Router,
              private activatedRoute: ActivatedRoute) { }

  ngOnInit(): void {
    this.fetchDashboardStats();
  }

  // Fetch Dashboard Stats: Total Users and Active Users
  fetchDashboardStats() {
    this.adminService.getUsers().subscribe(
      (response: any) => {
        this.totalUsers = response.data.totalUsers;
        this.activeUsers = response.data.activeUsers;
      },
      (error: any) => {
        console.error('Error fetching dashboard stats:', error);
        alert('Failed to load dashboard stats');
      }
    );
  }

  // Fetch all transactions
  fetchTransactions() {
    this.isLoading = true;
    this.adminService.getTransactions().subscribe(
      (response: any) => {
        this.transactions = response;
        this.isLoading = false;
      },
      (error: any) => {
        console.error('Error fetching transactions:', error);
        alert('Failed to load transactions');
        this.isLoading = false;
      }
    );
  }

  // Fetch loans
  fetchLoans() {
    this.isLoanLoading = true;
    this.adminService.getLoans().subscribe(
      (response: any) => {
        this.loans = response;
        this.isLoanLoading = false;
      },
      (error: any) => {
        console.error('Error fetching loans:', error);
        alert('Failed to load loans');
        this.isLoanLoading = false;
      }
    );
  }

  // Toggle Sidebar
  toggleSidebar() {
    this.isSidebarOpen = !this.isSidebarOpen;
  }

  // Logout
  logout() {
    this.router.navigate(['/']);
  }

  // Switch views based on sidebar link clicks
  showDashboard() {
    this.showDashboardSection = true;
    this.showTransactionsSection = false;
    this.showLoansSection = false;
  }

  showTransactions() {
    this.showDashboardSection = false;
    this.showTransactionsSection = true;
    this.showLoansSection = false;
    this.fetchTransactions(); // Fetch transactions when this section is shown
  }

  showLoans() {
    this.showDashboardSection = false;
    this.showTransactionsSection = false;
    this.showLoansSection = true;
    this.fetchLoans(); // Fetch loans when this section is shown
  }

  // Approve a loan
  approveLoan(loanId: string) {
    this.adminService.approveLoan(loanId).subscribe(
      (response) => {
        alert('Loan approved successfully!');
        this.fetchLoans(); // Refresh the loan list after approval
      },
      (error) => {
        console.error('Error approving loan:', error);
        alert('Failed to approve loan');
      }
    );
  }
}
