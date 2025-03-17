package org.example.taskmanagement.factory;


import org.example.taskmanagement.dto.UserDTO;
import org.example.taskmanagement.model.User;
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
