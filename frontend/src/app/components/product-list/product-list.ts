import { Component, OnInit, signal, Input, OnChanges, SimpleChanges } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { Product } from '../../model/product';
import { ProductManagerService } from '../../manager/product-manager.service';

@Component({
  selector: 'app-product-list',
  imports: [CurrencyPipe, MatCardModule, MatButtonModule],
  templateUrl: './product-list.html',
  styleUrl: './product-list.css'
})
export class ProductList implements OnInit, OnChanges {

  @Input() selectedCategoryId: number | null = null;

  products = signal<Product[]>([]);

  constructor(private productManager: ProductManagerService) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['selectedCategoryId']) {
      this.loadProducts();
    }
  }

  confrontaCategorie(a: Product, b: Product): number {
    return a.category.id - b.category.id;
  }

  loadProducts(): void {

    if (this.selectedCategoryId == null) {

      this.productManager.getAllProducts().subscribe({
        next: products => {
          products.sort(this.confrontaCategorie);
          this.products.set(products);
        },
        error: error => console.log('ERRORE:', error)
      });

    } else {

      this.productManager.getProductsByCategory(this.selectedCategoryId).subscribe({
        next: products => this.products.set(products),
        error: error => console.log('ERRORE:', error)
      });

    }
  }
}
