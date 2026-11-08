package org.example.backend.dto;

import org.example.backend.entities.User;

public record UserDTO(
        Integer id,
        String name,
        String surname,
        String email,
        User.Role role
) {
    public static UserDTO from(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getRole()
        );
    }
}