package org.example.userservice.service;

import lombok.AllArgsConstructor;

import org.example.userservice.model.User;
import org.example.userservice.repository.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =repository.findByEmail(username).get();

        if (repository.findByEmail(username).isEmpty())
            throw new UsernameNotFoundException("User not found");

        return User.builder()
                .email(user.getUsername())
                .password(user.getPassword())
                .build();
    }
}
