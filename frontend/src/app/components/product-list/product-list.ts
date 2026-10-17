import { Component, OnInit, signal } from '@angular/core';
import { Product } from '../../model/product';
import { ProductManagerService } from '../../manager/product-manager.service';

@Component({
  selector: 'app-product-list',
  imports: [],
  templateUrl: './product-list.html',
  styleUrl: './product-list.css'
})
export class ProductList implements OnInit {

  products = signal<Product[]>([]);

  constructor(private productManager: ProductManagerService) {}

  ngOnInit(): void {
    this.productManager.getAllProducts().subscribe({
      next: products => this.products.set(products),
      error: error => console.log('ERRORE:', error)
    });
  }
}
