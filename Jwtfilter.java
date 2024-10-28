package com.example.Authdemo.Filter;

import com.example.Authdemo.jwt.Jwtutil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class Jwtfilter extends OncePerRequestFilter {

    @Autowired
    private Jwtutil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(token);

            // Log the username extracted from the token
            System.out.println("Extracted username: " + username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (jwtUtil.validateToken(token, username)) {
                    // Create authentication object
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            username, null, new ArrayList<>()); // You can pass authorities if you have them
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    // Log successful authentication
                    System.out.println("Authentication successful for user: " + username);
                } else {
                    // Log token validation failure
                    System.out.println("Token validation failed");
                }
            }
        } else {
            // Log missing token
            System.out.println("No token found in the Authorization header");
        }

        chain.doFilter(request, response);
    }

}