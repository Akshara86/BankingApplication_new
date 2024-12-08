import { CommonModule, NgFor, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router'; // Import ActivatedRoute to get query params
import { LoanSectionComponent } from '../loan-section/loan-section.component';
import { AccountService } from '../Services/account.service';  // Import the AccountService
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [FormsModule, NgIf, NgFor, CommonModule, LoanSectionComponent],
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {
  userName = '';
  userId: string | null = null; // To hold the userId from URL
  accountTypes = ['Savings', 'Current', 'Fixed'];
  selectedAccountType: string = 'Savings';
  createdAccounts: any[] = [];
  showForm: boolean = false;
  formData = {
    aadhar: '',
    pan: '',
    gender: '',
    initialBalance: 0
  };
  userDetails: any = null;

  transactionAction: string = ''; // Action type: Withdraw, Deposit, Transfer, or Transactions
  transactionData = {
    amount: 0,
    fromAccount: '',
    toAccount: ''
  };
  showTransactionModal: boolean = false;
  transactionHistory: any[] = [];

  selectedAccountId: string = ''; // Add this in the class

  loans: any[] = []; // Array to hold loan data
  showLoansModal: boolean = false;

  // Card management
  cardApplicationFormVisible: boolean = false;
  selectedCardType: string = '';
  cardFormData = {
    userId: this.userId,
    cardLimit: null,
    cardType: ''
  };
  creditCardStatus: string = 'Apply';
  debitCardStatus: string = 'Apply';
  userCards: any[] = [];
  showCardsPopup: boolean = false;

  // Method to handle account selection
  selectAccount(accountId: string) {
    this.selectedAccountId = accountId; // Assign the accountId to a variable
  }

  // Inject ActivatedRoute to access URL params
  constructor(
    private router: Router,
    private accountService: AccountService,
    private activatedRoute: ActivatedRoute,
    private http: HttpClient
  ) { }

  ngOnInit(): void {
    // Fetch userId from the query parameters
    this.activatedRoute.queryParams.subscribe(params => {
      this.userId = params['userId']; // Get userId from the URL query params
      console.log('User ID:', this.userId);

      if (this.userId) {
        // Fetch user details and accounts using the userId
        this.fetchUserDetails(this.userId);
        this.fetchAccounts();
        this.fetchUserCards();
      }
    });
  }

  // Fetch user details based on userId
  fetchUserDetails(userId: string) {
    this.accountService.getUserDetails(userId).subscribe(
      (response) => {
        this.userDetails = response;
        this.userName = response.data.username
        console.log('User Details:', this.userDetails);
      },
      (error) => {
        console.error('Error fetching user details:', error);
      }
    );
  }

  // Fetch accounts for the user
  fetchAccounts() {
    if (this.userId) {
      this.accountService.getAccountsByUserId(this.userId).subscribe(
        (accounts) => {
          this.createdAccounts = accounts;
        },
        (error) => {
          console.error('Error fetching accounts', error);
        }
      );
    }
  }

  // Create account with API call
  createAccount(accountType: string) {
    // Check if the account type is already created
    if (this.isAccountCreated(accountType)) {
      // If the account already exists, set the selected account type and show the "Close Account" and "Update Account" buttons
      this.selectedAccountType = accountType;
      this.showForm = false; // Hide the form for account creation
    } else {
      // If the account does not exist, show the form for account creation
      this.selectedAccountType = accountType;
      this.showForm = true;
    }
  }

  // Method to close the account
  closeAccount(accountType: string) {
    // Find the account to be closed
    const accountToClose = this.createdAccounts.find(
      (account) => account.accountType === accountType
    );

    if (accountToClose) {
      // Call the service to delete the account
      this.accountService.closeAccount(accountToClose.accountId).subscribe(
        () => {
          // Remove the closed account from the list
          this.createdAccounts = this.createdAccounts.filter(
            (account) => account.accountId !== accountToClose.accountId
          );
          alert('Account closed successfully');
        },
        (error) => {
          console.error('Error closing account', error);
          alert('Error: ' + error.error.message);
        }
      );
    }
    this.showForm = false;
  }

  // Submit account details using API
  submitAccountDetails() {
    if (this.formData.aadhar && this.formData.pan && this.formData.gender && this.formData.initialBalance !== null) {
      const newAccount = {
        accountType: this.selectedAccountType,
        balance: this.formData.initialBalance,
        aadharNumber: this.formData.aadhar,
        pan: this.formData.pan,
        gender: this.formData.gender,
        userId: this.userId // Include userId when creating the account
      };

      if (!this.userDetails) {
        this.userDetails = { ...this.formData }; // Store the user details if not already stored
      }

      this.accountService.createAccount(newAccount).subscribe(
        (response) => {
          this.createdAccounts.push(response); // Add the new account to the list
          this.showForm = false;
        },

        (error) => {
          alert('Error creating account');
          console.error('Error creating account', error);
        }
      );
    } else {
      alert('Please fill in all the details.');
    }
  }

  // Check if account type is already created
  isAccountCreated(accountType: string): boolean {
    return this.createdAccounts.some(account => account.accountType === accountType);
  }

  // Handle account-related actions (e.g., Withdraw, Deposit, etc.)
  performAction(action: string) {
    console.log(`${action} action performed`);
  }

  // Update account details using API
  updateAccountDetails(accountType: string) {
    const account = this.createdAccounts.find(acc => acc.type === accountType);
    if (account) {
      // Logic for updating account details
      // You can use a similar approach to your `submitAccountDetails()` function
      this.selectedAccountType = accountType;  // Update to the selected account type
      this.showForm = true;  // Show the form to update the account
      this.formData = { ...account };  // Pre-fill the form data with existing account details
    }
  }

  // user.component.ts
  createOrCloseAccount(accountType: string): void {
    // If the account is already created, close it.
    if (this.isAccountCreated(accountType)) {
      // Close account logic here
      this.closeAccount(accountType);
      this.showForm = false; // Hide the form
    } else {
      // Create account logic here
      this.createAccount(accountType);
      this.showForm = true; // Show the form for new account
    }
  }

  createBtn(accountType: string): void {
    this.showForm = !this.showForm; // Hide the form
  }


  // Open Transaction Modal
  performActionTransaction(action: string, accountId?: string) {
    this.transactionAction = action;
    this.transactionData = {
      amount: 0,
      fromAccount: accountId || '',  // Default to an empty string if accountId is not provided
      toAccount: ''
    };
    if (accountId) {
      this.selectedAccountId = accountId; // Capture the accountId for transactions
    }

    if (action === 'Transactions' && accountId) {
      this.fetchTransactionHistory(accountId);
    } else {
      this.showTransactionModal = true;
    }
  }

  // Close Transaction Modal
  closeTransactionModal() {
    this.showTransactionModal = false;
    this.transactionData = { amount: 0, fromAccount: '', toAccount: '' };
  }

  // Submit Transaction
  submitTransaction() {
    const { amount, fromAccount, toAccount } = this.transactionData;

    if (this.transactionAction === 'Withdraw' || this.transactionAction === 'Deposit') {
      // Pass the selected account's ID for Withdraw/Deposit
      const accountId = this.selectedAccountId; // Ensure `selectedAccountId` holds the proper value
      if (!accountId) {
        alert('Account ID is required for this transaction.');
        return;
      }

      this.accountService.performTransaction(this.transactionAction, accountId, { amount })
        .subscribe(
          (response) => {
            alert(`${this.transactionAction} successful!`);
            this.closeTransactionModal();
            this.fetchAccounts(); // Refresh account details
          },
          (error) => {
            console.error(`Error during ${this.transactionAction}:`, error);
            alert('Transaction failed! ' + error.error.message);
          }
        );
    } else if (this.transactionAction === 'Transfer') {
      // Handle Transfer where `accountId` is not part of the URL
      if (!fromAccount || !toAccount) {
        alert('Both From Account and To Account are required for transfer.');
        return;
      }

      this.accountService.transferFunds({ fromAccountId: fromAccount, toAccountId: toAccount, amount })
        .subscribe(
          (response) => {

            console.log(response);

            alert('Transfer successful!');
            this.closeTransactionModal();
            this.fetchAccounts(); // Refresh account details
          },
          (error) => {
            console.error('Error during transfer:', error);
            alert('Transfer failed! ' + error.error.message);
          }
        );
    }
  }

  // Fetch Transaction History
  fetchTransactionHistory(accountId: string) {
    this.accountService.getTransactionHistory(accountId).subscribe(
      (transactions) => {

        transactions.forEach(transaction => {
          if (typeof transaction.timestamp === 'string') {
            // For ISO string or Unix timestamp, convert to Date
            transaction.timestamp = new Date(transaction.timestamp);
          }
        });

        this.transactionHistory = transactions;
        this.showTransactionModal = true;
      },
      (error) => {
        console.error('Error fetching transaction history:', error);
        alert('Could not fetch transaction history!');
      }
    );
  }

  logoutBtn(userId: string) {
    this.accountService.getLogout(userId).subscribe(
      (response) => {
        console.log(response);
        alert(response.message)
        this.router.navigate(['/'])
      },
      (error) => {
        alert('Error: ' + error.error.message)
        console.error('Error', error.error.message);
      }
    );
  }

  // Close card application form
  closeCardForm() {
    this.cardApplicationFormVisible = false;
    this.resetCardForm();
  }

  closeCardApplicationForm() {
    this.cardApplicationFormVisible = false;
    this.resetCardForm(); // Optional, reset form data if needed
  }
  showCardApplicationForm() {
    this.cardApplicationFormVisible = true;
  }


  openLoansModal() {
    if (this.userId) {
      this.fetchLoans(this.userId);  // Fetch loans if the userId is available
    }
    this.showLoansModal = true;  // Show the modal
  }

  // Method to close the loans modal
  closeLoansModal() {
    this.showLoansModal = false;
  }

  // Fetch loans from the API
  fetchLoans(userId: string) {
    const loanUrl = `http://localhost:8092/loans/user/${userId}`;
    this.http.get<any[]>(loanUrl).subscribe(
      (data) => {
        this.loans = data;  // Store the loan data in the loans array
        console.log('Loans:', this.loans);
      },
      (error) => {
        console.error('Error fetching loans:', error);
        alert('Failed to fetch loan data');
      }
    );
  }

  // Card Application
  openCardApplicationForm(cardType: string) {
    this.selectedCardType = cardType;
    this.cardApplicationFormVisible = true;
  }


  resetCardForm() {
    this.cardFormData = { userId: this.userId,
      cardLimit: null,
      cardType: ''};
  }

  submitCardApplication() {
    const cardData = { ...this.cardFormData, type: this.selectedCardType };

    this.accountService.submitCardApplication(cardData).subscribe(
      response => {
        alert('Card application submitted successfully!');
        this.cardApplicationFormVisible = false;
        this.resetCardForm();
        this.fetchUserCards(); // Refresh user cards
      },
      error => {
        console.error('Error submitting card application', error);
        alert('Error: ' + error.error.message);
      }
    );
  }

  // Fetch user cards
  fetchUserCards() {
    if (this.userId) {
      this.accountService.getUserCards(this.userId).subscribe(
        cards => {
          this.userCards = cards;
        },
        error => {
          console.error('Error fetching user cards:', error);
        }
      );
    }
  }

  // Block card
  blockCard(cardId: string) {
    this.accountService.blockCard(cardId).subscribe(
      () => {
        alert('Card blocked successfully!');
        this.fetchUserCards(); // Refresh card list
      },
      error => {
        console.error('Error blocking card:', error);
        alert('Error: ' + error.error.message);
      }
    );
  }
  
  openCardsPopup() {
    this.showCardsPopup = true;
  }

  closeCardsPopup() {
    this.showCardsPopup = false;
  }
}
