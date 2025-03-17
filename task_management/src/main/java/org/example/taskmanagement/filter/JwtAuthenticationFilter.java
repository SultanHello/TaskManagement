package org.example.taskmanagement.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

import org.example.taskmanagement.service.JwtService;
import org.example.taskmanagement.service.MyUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final MyUserDetailsService myUserDetailsService;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if(authHeader==null||!authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        String token =authHeader.substring(7);
        String idNumber =jwtService.extractId(token);
        if(idNumber!=null&& SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails = myUserDetailsService.loadUserByUsername(idNumber);
            if(jwtService.isValid(token,userDetails)){
                UsernamePasswordAuthenticationToken authenticationToken=
                        new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }else{

            }
        }
        filterChain.doFilter(request,response);
    }
}
