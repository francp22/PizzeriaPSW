package org.example.backend.services;

import org.example.backend.entities.Cart;
import org.example.backend.entities.User;
import org.example.backend.repositories.UserRepository;
import org.example.backend.support.exceptions.MailUserAlreadyExistsException;
import org.example.backend.support.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public User registerUser(User user) throws MailUserAlreadyExistsException {
        if ( userRepository.existsByEmail(user.getEmail()) ) {
            throw new MailUserAlreadyExistsException();
        }
        if (user.getRole()==User.Role.CUSTOMER) {
            Cart cart = new Cart();

            user.setCart(cart);
            cart.setUser(user);
        }
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(String email) throws UserNotFoundException {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UserNotFoundException();
        }

        return user;
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}