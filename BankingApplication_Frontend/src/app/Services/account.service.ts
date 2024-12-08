import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  private apiUrl = 'http://localhost:8092'; // Replace with your actual API URL

  constructor(private http: HttpClient) {}

  // Get user details by userId
  getUserDetails(userId: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/api/users/${userId}`);
  }

  // Logout user by userId
  getLogout(userId: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/api/users/logout?userId=${userId}`);
  }

  // Get accounts by userId
  getAccountsByUserId(userId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/accounts/user/${userId}`);
  }

  // Create a new account
  createAccount(account: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/accounts/open`, account);
  }

  // Update account
  updateAccount(accountId: string, updatedDetails: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/accounts/${accountId}`, updatedDetails);
  }

  // Close an account
  closeAccount(accountId: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/accounts/close/${accountId}`);
  }

  // Perform transaction (Withdraw/Deposit)
  performTransaction(
    action: string,
    accountId: string,
    data: { amount: number }
  ): Observable<any> {
    return this.http.put(`${this.apiUrl}/accounts/${action.toLowerCase()}/${accountId}`, data);
  }

  // Transfer funds
  transferFunds(data: { fromAccountId: string; toAccountId: string; amount: number }): Observable<any> {
    return this.http.put(`${this.apiUrl}/accounts/transfer`, data);
  }

  // Get transaction history
  getTransactionHistory(accountId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/accounts/transactions/${accountId}`);
  }

  // Submit card application
  submitCardApplication(cardData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/cards/apply`, cardData);
  }

  // Get cards by userId
  getUserCards(userId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/cards/user/${userId}`);
  }

  // Block a card
  blockCard(cardId: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/cards/block/${cardId}`, {});
  }
}
