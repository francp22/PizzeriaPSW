import { Component, OnInit, signal, EventEmitter, Output } from '@angular/core';
import { MatChipsModule } from '@angular/material/chips';
import { Category } from '../../model/category';
import { CategoryManagerService } from '../../manager/category-manager.service';

@Component({
  imports: [MatChipsModule],
  selector: 'app-category-list',
  styleUrl: './category-list.css',
  templateUrl: './category-list.html',
})
export class CategoryList implements OnInit {

  categories = signal<Category[]>([]);
  selectedCategoryId = signal<number | null>(null);

  @Output() categorySelected = new EventEmitter<number | null>();

  constructor(private categoryManager: CategoryManagerService) {}

  ngOnInit(): void {
    this.categoryManager.getAllCategories().subscribe({
      next: categories => this.categories.set(categories),
      error: error => console.log('ERRORE:', error)
    });
  }

  selectCategory(categoryId: number | null): void {
    this.selectedCategoryId.set(categoryId);
    this.categorySelected.emit(categoryId);
  }
}
