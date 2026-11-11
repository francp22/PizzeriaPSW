package org.example.backend.controllers;

import jakarta.validation.Valid;
import org.example.backend.dto.LoginDTO;
import org.example.backend.dto.UserDTO;
import org.example.backend.dto.UserRegistrationDTO;
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
    public ResponseEntity<?> create(@Valid @RequestBody UserRegistrationDTO dto) {
        try {
            User added = userService.registerUser(dto);
            return new ResponseEntity<>(UserDTO.from(added), HttpStatus.CREATED);
        } catch (MailUserAlreadyExistsException e) {
            return new ResponseEntity<>("User already exists!", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public List<UserDTO> getAll() {
        return userService.getAllUsers().stream().map(UserDTO::from).toList();
    }

    @GetMapping("/search/by_email")
    public ResponseEntity<?> getByEmail(@RequestParam String email) {
        try {
            return new ResponseEntity<>(UserDTO.from(userService.getUserByEmail(email)), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO dto){
        try{
            User user=userService.login(dto.email(), dto.password());
            return new ResponseEntity<>(UserDTO.from(user),HttpStatus.OK);
        }catch(UserNotFoundException e){
            return new ResponseEntity<>("Email o password non corretti",HttpStatus.UNAUTHORIZED);
        }
    }
}