import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private registerUrl = 'http://localhost:8092/api/users/register';
  private loginUrl = 'http://localhost:8092/api/users/login';

  constructor(private http: HttpClient) {}

  registerUser(user: { username: string; email: string; password: string }): Observable<any> {

    return this.http.post<any>(this.registerUrl, user);
  }

  loginUser(user: { email: string; password: string,role:string }): Observable<any> {
    return this.http.post<any>(this.loginUrl, user);
  }
}
