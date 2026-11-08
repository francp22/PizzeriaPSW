import { Component, OnInit, signal } from '@angular/core';
import { Category } from '../../model/category';
import { CategoryManagerService } from '../../manager/category-manager.service';

@Component({
  imports: [],
  selector: 'app-category-list',
  styleUrl: './category-list.css',
  templateUrl: './category-list.html',
})
export class CategoryList implements OnInit {

  categories = signal<Category[]>([]);

  constructor(private categoryManager: CategoryManagerService) {}

  ngOnInit(): void {
    this.categoryManager.getAllCategories().subscribe({
      next: categories => this.categories.set(categories),
      error: error => console.log('ERRORE:', error)
    });
  }
}
