package org.example.backend.services;

import org.example.backend.repositories.CartItemRepository;
import org.example.backend.repositories.CartRepository;
import org.example.backend.repositories.OrderItemRepository;
import org.example.backend.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;
}
