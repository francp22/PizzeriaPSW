import { Component } from '@angular/core';
import { ProductList } from './components/product-list/product-list';
import { CategoryList } from './components/category-list/category-list';
import { UserRegister } from './components/user-register/user-register';

@Component({
  selector: 'app-root',
  imports: [ProductList, CategoryList, UserRegister],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {}
