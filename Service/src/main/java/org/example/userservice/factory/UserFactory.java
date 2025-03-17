package org.example.userservice.factory;


import org.example.userservice.dto.UserDTO;
import org.example.userservice.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserFactory {

    public User create(UserDTO userDTO) {
        return User.builder()
                .email(userDTO.getEmail())
                .role(userDTO.getRole())
                .password(userDTO.getPassword())
                .build();
    }
}
