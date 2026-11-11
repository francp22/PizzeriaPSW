
import { Routes } from '@angular/router';
import { Menu } from './components/menu/menu';
import { Login } from './components/login/login';
import { UserRegister } from './components/user-register/user-register';

export const routes: Routes = [
  { path: '', component: Menu },
  { path: 'login', component: Login },
  { path: 'registrazione', component: UserRegister }
];
