import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UserLogin } from '../../model/user';
import { UserManagerService } from '../../manager/user-manager.service';
import { Router } from '@angular/router';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {

  user: UserLogin = {
    email: '',
    password: ''
  };


  constructor(private userManager: UserManagerService,private router: Router) {}

  login(): void {
    this.userManager.loginUser(this.user).subscribe({
      next: user => {
        console.log('Login effettuato:', user);
        alert('Benvenuto ' + user.name);
        this.router.navigate(['/']);

      },
      error: error => {
        console.log('ERRORE:', error);
        alert('Email o password non corretti');
      }
    });
  }
}
