package org.example.backend.controllers;

import org.example.backend.entities.User;
import org.example.backend.support.exceptions.*;
import org.example.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity create(@RequestBody User user) {
        try {
            User added = userService.registerUser(user);
            return new ResponseEntity<>(added, HttpStatus.OK);

        } catch (MailUserAlreadyExistsException e) {
            return new ResponseEntity<>(
                    "User already exists!",
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public List<User> getAll() {
        return userService.getAllUsers();
    }

    @GetMapping("/search/by_email")
    public ResponseEntity getByEmail(
            @RequestParam String email) {

        try {
            User user = userService.getUserByEmail(email);
            return new ResponseEntity<>(user, HttpStatus.OK);

        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(
                    "User not found!",
                    HttpStatus.BAD_REQUEST);
        }
    }
}