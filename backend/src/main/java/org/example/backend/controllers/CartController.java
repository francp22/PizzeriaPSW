package org.example.backend.controllers;

import org.example.backend.entities.Cart;
import org.example.backend.support.exceptions.*;
import org.example.backend.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity getCart(
            @PathVariable Integer userId) {

        try {
            Cart cart = cartService.getCartByUser(userId);
            return new ResponseEntity<>(cart, HttpStatus.OK);

        } catch (CartNotFoundException e) {
            return new ResponseEntity<>(
                    "Cart not found!",
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{userId}/products/{productId}")
    public ResponseEntity addProduct(
            @PathVariable Integer userId,
            @PathVariable Integer productId,
            @RequestParam int quantity) {

        try {
            cartService.addProductToCart(
                    userId,
                    productId,
                    quantity);

            return new ResponseEntity<>(
                    "Product added to cart!",
                    HttpStatus.OK);

        } catch (CartNotFoundException e) {
            return new ResponseEntity<>(
                    "Cart not found!",
                    HttpStatus.BAD_REQUEST);

        } catch (ProductNotFoundException e) {
            return new ResponseEntity<>(
                    "Product not found!",
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{userId}/products/{productId}")
    public ResponseEntity removeProduct(
            @PathVariable Integer userId,
            @PathVariable Integer productId) {

        try {
            cartService.removeProductFromCart(
                    userId,
                    productId);

            return new ResponseEntity<>(
                    "Product removed from cart!",
                    HttpStatus.OK);

        } catch (CartNotFoundException e) {
            return new ResponseEntity<>(
                    "Cart not found!",
                    HttpStatus.BAD_REQUEST);

        } catch (CartItemNotFoundException e) {
            return new ResponseEntity<>(
                    "Product not found in cart!",
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{userId}/products/{productId}")
    public ResponseEntity updateQuantity(
            @PathVariable Integer userId,
            @PathVariable Integer productId,
            @RequestParam int quantity) {

        try {
            cartService.updateProductQuantity(
                    userId,
                    productId,
                    quantity);

            return new ResponseEntity<>(
                    "Quantity updated!",
                    HttpStatus.OK);

        } catch (CartNotFoundException e) {
            return new ResponseEntity<>(
                    "Cart not found!",
                    HttpStatus.BAD_REQUEST);

        } catch (CartItemNotFoundException e) {
            return new ResponseEntity<>(
                    "Product not found in cart!",
                    HttpStatus.BAD_REQUEST);

        } catch (InvalidQuantityException e) {
            return new ResponseEntity<>(
                    "Invalid quantity!",
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{userId}/items")
    public ResponseEntity clearCart(
            @PathVariable Integer userId) {

        try {
            cartService.clearCart(userId);

            return new ResponseEntity<>(
                    "Cart cleared!",
                    HttpStatus.OK);

        } catch (CartNotFoundException e) {
            return new ResponseEntity<>(
                    "Cart not found!",
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{userId}/total")
    public ResponseEntity getTotal(
            @PathVariable Integer userId) {

        try {
            double total = cartService.getCartTotal(userId);

            return new ResponseEntity<>(
                    total,
                    HttpStatus.OK);

        } catch (CartNotFoundException e) {
            return new ResponseEntity<>(
                    "Cart not found!",
                    HttpStatus.BAD_REQUEST);

        } catch (CartEmptyException e) {
            return new ResponseEntity<>(
                    "Cart is empty!",
                    HttpStatus.BAD_REQUEST);
        }
    }
}