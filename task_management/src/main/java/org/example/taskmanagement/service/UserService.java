package org.example.taskmanagement.service;


import lombok.RequiredArgsConstructor;
import org.example.taskmanagement.dto.UpdateUserDTO;
import org.example.taskmanagement.dto.UserDTO;
import org.example.taskmanagement.factory.UserFactory;
import org.example.taskmanagement.model.User;
import org.example.taskmanagement.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserFactory userFactory;
    private final PasswordEncoder passwordEncoder;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public void addUser(UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        User user = userFactory.create(userDTO);
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        userRepository.delete(user);
    }

    public User updateUser(Long id, UpdateUserDTO updateUserDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if(updateUserDTO.getEmail()!=null){
            user.setEmail(updateUserDTO.getEmail());
        }
        if(updateUserDTO.getPassword()!=null){
            user.setPassword(passwordEncoder.encode(updateUserDTO.getPassword()));
        }
        return userRepository.save(user);
    }
}
