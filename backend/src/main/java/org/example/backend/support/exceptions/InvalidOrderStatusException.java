package org.example.backend.support.exceptions;

public class InvalidOrderStatusException extends Exception {

    public InvalidOrderStatusException() {
        super("Invalid order status");
    }
}
