package org.example.taskmanagement.config;


import lombok.AllArgsConstructor;

import org.example.taskmanagement.filter.JwtAuthenticationFilter;
import org.example.taskmanagement.service.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@AllArgsConstructor
public class SecurityConfig {
    private final MyUserDetailsService myUserDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        try {
            return http.csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth->auth.requestMatchers("/api/auth/register", "/api/auth/login",
                                    "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                            .anyRequest().authenticated())
                    .userDetailsService(myUserDetailsService)
                    .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                    .build();
        }catch (Exception e){
            throw new RuntimeException("");
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager manager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }
}
