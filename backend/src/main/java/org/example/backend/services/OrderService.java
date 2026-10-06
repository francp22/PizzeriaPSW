package org.example.backend.services;

import org.example.backend.entities.Cart;
import org.example.backend.entities.CartItem;
import org.example.backend.entities.Order;
import org.example.backend.entities.OrderItem;

import org.example.backend.repositories.OrderItemRepository;
import org.example.backend.repositories.OrderRepository;
import org.example.backend.support.exceptions.CartEmptyException;
import org.example.backend.support.exceptions.CartNotFoundException;
import org.example.backend.support.exceptions.InvalidOrderStatusException;
import org.example.backend.support.exceptions.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartService cartService;


    @Transactional(readOnly = false)
    public void createOrder(Integer userId,
                            Order.OrderType type,
                            String street,
                            String city,
                            String phoneNumber)
            throws CartNotFoundException, CartEmptyException {

        Cart cart = cartService.getCartByUser(userId);

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new CartEmptyException();
        }

        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("Telefono richiesto");
        }

        if (type == Order.OrderType.DELIVERY) {
            if (street == null || street.isBlank()
                    || city == null || city.isBlank()) {
                throw new IllegalArgumentException(
                        "via e citta necessari");
            }
        }

        Order order = new Order();
        order.setCreationDate(LocalDateTime.now());
        order.setType(type);
        order.setStatus(Order.OrderStatus.PREPARING);
        order.setStreet(street);
        order.setCity(city);
        order.setPhoneNumber(phoneNumber);
        order.setUser(cart.getUser());

        List<OrderItem> orderItems = new ArrayList<>();


        for (CartItem cartItem : cart.getItems()) {

            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(cartItem.getProduct().getPrice());

            orderItems.add(orderItem);


        }
        double total = cartService.getCartTotal(userId);
        order.setTotal(total);

        orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);

        cartService.clearCart(userId);
    }

    @Transactional(readOnly = true)
    public List<Order> showUserOrders(Integer userId) {
        return orderRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Order getOrderById(Integer orderId)
            throws OrderNotFoundException {

        Order order = orderRepository.findById(orderId).orElse(null);

        if (order == null) {
            throw new OrderNotFoundException();
        }

        return order;
    }

    @Transactional(readOnly = false)
    public void updateOrderStatus(Integer orderId,
                                  Order.OrderStatus status)
            throws OrderNotFoundException, InvalidOrderStatusException {

        Order order = getOrderById(orderId);

        if (order.getStatus() == Order.OrderStatus.DELIVERED) {
            throw new InvalidOrderStatusException();
        }

        if (order.getType() == Order.OrderType.TAKEAWAY
                && status == Order.OrderStatus.OUT_FOR_DELIVERY) {
            throw new InvalidOrderStatusException();
        }

        if (order.getStatus() == Order.OrderStatus.PREPARING
                && status != Order.OrderStatus.READY) {
            throw new InvalidOrderStatusException();
        }

        if (order.getStatus() == Order.OrderStatus.READY) {

            if (order.getType() == Order.OrderType.DELIVERY
                    && status != Order.OrderStatus.OUT_FOR_DELIVERY) {
                throw new InvalidOrderStatusException();
            }

            if (order.getType() == Order.OrderType.TAKEAWAY
                    && status != Order.OrderStatus.DELIVERED) {
                throw new InvalidOrderStatusException();
            }
        }

        if (order.getStatus() == Order.OrderStatus.OUT_FOR_DELIVERY
                && status != Order.OrderStatus.DELIVERED) {
            throw new InvalidOrderStatusException();
        }

        order.setStatus(status);

        orderRepository.save(order);
    }

}
