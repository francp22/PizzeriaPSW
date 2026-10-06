package org.example.backend.support.exceptions;

public class OrderNotFoundException extends Exception {

    public OrderNotFoundException() {
        super("Order not found");
    }
}
