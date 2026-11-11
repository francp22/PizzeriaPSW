import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { UserManagerService } from '../../manager/user-manager.service';
import { UserRegistration } from '../../model/user';
import { RouterLink } from '@angular/router';

@Component({
  imports: [FormsModule, RouterLink],
  selector: 'app-user-register',
  styleUrl: './user-register.css',
  templateUrl: './user-register.html',
})
export class UserRegister {

  user: UserRegistration = {
    name: '',
    surname: '',
    email: '',
    password: ''
  };

  constructor(private userManager: UserManagerService, private router: Router) {}

  register(): void {
    this.userManager.registerUser(this.user).subscribe({
      next: user => {
        console.log('Utente registrato:', user);
        alert('Registrazione completata!');
        this.router.navigate(['/']);
      },
      error: error => {
        console.log('ERRORE:', error);
        alert('Errore durante la registrazione');
      }
    });
  }
}
