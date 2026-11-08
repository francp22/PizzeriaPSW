package org.example.backend.services;

import org.example.backend.dto.UserRegistrationDTO;
import org.example.backend.entities.Cart;
import org.example.backend.entities.User;
import org.example.backend.repositories.UserRepository;
import org.example.backend.support.exceptions.MailUserAlreadyExistsException;
import org.example.backend.support.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public User registerUser(UserRegistrationDTO dto) throws MailUserAlreadyExistsException {
        if (userRepository.existsByEmail(dto.email())) {
            throw new MailUserAlreadyExistsException();
        }

        User user = new User();
        user.setName(dto.name());
        user.setSurname(dto.surname());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRole(User.Role.CUSTOMER);

        if (user.getRole() == User.Role.CUSTOMER) {
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