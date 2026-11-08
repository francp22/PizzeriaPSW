import { Component } from '@angular/core';
import { User } from '../../model/user';
import { UserManagerService } from '../../manager/user-manager.service';
import { FormsModule } from '@angular/forms';
import { UserRegistration } from '../../model/user';

@Component({
  imports: [FormsModule],
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

  constructor(private userManager: UserManagerService) {}

  register(): void {
    this.userManager.registerUser(this.user).subscribe({
      next: user => {
        console.log('Utente registrato:', user);
        alert('Registrazione completata!');
      },
      error: error => {
        console.log('ERRORE:', error);
        alert('Errore durante la registrazione');
      }
    });
  }
}
