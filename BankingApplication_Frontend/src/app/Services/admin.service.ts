import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class AdminService {
    private apiUrl = 'http://localhost:8092'; // Replace with your API base URL
    
    getTransactions(): Observable<any> {
        return this.http.get<any>(`${this.apiUrl}/api/transactions/all`);
      }
    constructor(private http: HttpClient) {}

    // Fetch all users
    getUsers(): Observable<any> {
        return this.http.get(`${this.apiUrl}/api/users/admin/user-count`);
    }

    getLoans(): Observable<any> {
        return this.http.get<any>(`${this.apiUrl}/loans/admin/all`);
      }
    
      // Approve a loan by ID
      approveLoan(loanId: string): Observable<any> {
        return this.http.put<any>(`${this.apiUrl}/loans/approve/${loanId}`, {});
      }

}
