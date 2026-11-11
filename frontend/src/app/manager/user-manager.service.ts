import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { User, UserRegistration, UserLogin } from '../model/user';

@Injectable({
  providedIn: 'root'
})
export class UserManagerService {

  private serverUrl = 'http://localhost:8080';

  currentUser = signal<User | null>(
    this.loadUser()
  );

  isLoggedIn = computed(() => this.currentUser() !== null);

  isAdmin = computed(() => this.currentUser()?.role === 'ADMIN');

  constructor(private http: HttpClient) {}

  private loadUser(): User | null {
    const user = localStorage.getItem('currentUser');

    if (user != null) {
      return JSON.parse(user);
    }

    return null;
  }

  registerUser(user: UserRegistration): Observable<User> {
    return this.http.post<User>(this.serverUrl + '/users', user);
  }

  loginUser(user: UserLogin): Observable<User> {
    return this.http.post<User>(this.serverUrl + '/users/login', user).pipe(
      tap(user => {
        this.currentUser.set(user);
        localStorage.setItem('currentUser', JSON.stringify(user));
      })
    );
  }

  logout(): void {
    this.currentUser.set(null);
    localStorage.removeItem('currentUser');
  }
}
