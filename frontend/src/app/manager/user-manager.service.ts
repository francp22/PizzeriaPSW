import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User, UserRegistration } from '../model/user';

@Injectable({
  providedIn: 'root'
})
export class UserManagerService {

  private serverUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  registerUser(user: UserRegistration): Observable<User> {
    return this.http.post<User>(this.serverUrl + '/users', user);
  }
}
