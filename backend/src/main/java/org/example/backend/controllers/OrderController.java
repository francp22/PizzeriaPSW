package org.example.backend.controllers;

import org.example.backend.entities.Order;
import org.example.backend.support.exceptions.*;
import org.example.backend.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/{userId}")
    public ResponseEntity createOrder(
            @PathVariable Integer userId,
            @RequestBody Order order) {

        try {
            orderService.createOrder(
                    userId,
                    order.getType(),
                    order.getStreet(),
                    order.getCity(),
                    order.getPhoneNumber());

            return new ResponseEntity<>(
                    "Order created successfully!",
                    HttpStatus.OK);

        } catch (CartNotFoundException e) {
            return new ResponseEntity<>(
                    "Cart not found!",
                    HttpStatus.BAD_REQUEST);

        } catch (CartEmptyException e) {
            return new ResponseEntity<>(
                    "Cart is empty!",
                    HttpStatus.BAD_REQUEST);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{userId}")
    public List<Order> getUserOrders(
            @PathVariable Integer userId) {

        return orderService.showUserOrders(userId);
    }

    @GetMapping("/detail/{orderId}")
    public ResponseEntity getOrderById(
            @PathVariable Integer orderId) {

        try {
            Order order = orderService.getOrderById(orderId);

            return new ResponseEntity<>(
                    order,
                    HttpStatus.OK);

        } catch (OrderNotFoundException e) {
            return new ResponseEntity<>(
                    "Order not found!",
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity updateStatus(
            @PathVariable Integer orderId,
            @RequestParam Order.OrderStatus status) {

        try {
            orderService.updateOrderStatus(
                    orderId,
                    status);

            return new ResponseEntity<>(
                    "Order status updated!",
                    HttpStatus.OK);

        } catch (OrderNotFoundException e) {
            return new ResponseEntity<>(
                    "Order not found!",
                    HttpStatus.BAD_REQUEST);

        } catch (InvalidOrderStatusException e) {
            return new ResponseEntity<>(
                    "Invalid order status!",
                    HttpStatus.BAD_REQUEST);
        }
    }
}