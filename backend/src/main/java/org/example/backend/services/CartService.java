package org.example.backend.services;

import org.example.backend.entities.Cart;
import org.example.backend.entities.CartItem;
import org.example.backend.entities.Product;
import org.example.backend.repositories.CartItemRepository;
import org.example.backend.repositories.CartRepository;
import org.example.backend.repositories.ProductRepository;
import org.example.backend.repositories.UserRepository;
import org.example.backend.support.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public Cart getCartByUser(Integer userId) throws CartNotFoundException {
        Cart cart = cartRepository.findByUserId(userId);

        if (cart == null) {
            throw new CartNotFoundException();
        }

        return cart;
    }

    @Transactional(readOnly = false)
    public void addProductToCart(Integer userId, Integer productId, int quantity)
            throws CartNotFoundException, ProductNotFoundException,InvalidQuantityException {
        if (quantity <= 0) {
            throw new InvalidQuantityException();
        }

        Cart cart = getCartByUser(userId);

        Optional<Product> productOptional = productRepository.findById(productId);

        if (productOptional.isEmpty()) {
            throw new ProductNotFoundException();
        }

        Product product = productOptional.get();

        CartItem item = cartItemRepository.findByCartIdAndProductId(
                cart.getId(), productId);

        if (item != null) {
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);

            cartItemRepository.save(newItem);
        }
    }

    @Transactional(readOnly = false)
    public void removeProductFromCart(Integer userId, Integer productId)
            throws CartNotFoundException, CartItemNotFoundException {

        Cart cart = getCartByUser(userId);

        CartItem item = cartItemRepository.findByCartIdAndProductId(
                cart.getId(), productId);

        if (item == null) {
            throw new CartItemNotFoundException();
        }

        cartItemRepository.delete(item);
    }

    @Transactional(readOnly = false)
    public void updateProductQuantity(Integer userId, Integer productId, int quantity)
            throws CartNotFoundException, CartItemNotFoundException {

        Cart cart = getCartByUser(userId);

        CartItem item = cartItemRepository.findByCartIdAndProductId(
                cart.getId(), productId);

        if (item == null) {
            throw new CartItemNotFoundException();
        }
        if (quantity < 0) {
            throw new InvalidQuantityException();
        }
        if (quantity == 0) {
            removeProductFromCart(userId, productId);
        }
        item.setQuantity(quantity);

        cartItemRepository.save(item);
    }

    @Transactional(readOnly = false)
    public void clearCart(Integer userId)
            throws CartNotFoundException {

        Cart cart = getCartByUser(userId);

        cartItemRepository.deleteAll(cart.getItems());
    }

    @Transactional(readOnly = true)
    public double getCartTotal(Integer userId) throws CartNotFoundException,CartEmptyException {

        Cart cart = getCartByUser(userId);

        double total = 0;
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new CartEmptyException();
        }

        for (CartItem item : cart.getItems()) {
            total += item.getQuantity() * item.getProduct().getPrice();
        }

        return total;
    }

}