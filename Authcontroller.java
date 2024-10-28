package com.example.Authdemo.controller;

import com.example.Authdemo.entity.User;
import com.example.Authdemo.jwt.Jwtutil;
import com.example.Authdemo.repository.Userrepository;
import com.example.Authdemo.service.Userservice;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

@RestController
public class Authcontroller {

    @Autowired
    private Userservice userService;

    @Autowired
    private Userrepository userrepository;
    @Autowired
    private Jwtutil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;  // Inject the password encoder


    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody User user, BindingResult result) {
        if (result.hasErrors()) {
            // Return the first validation error message
            return ResponseEntity.badRequest().body(result.getFieldError().getDefaultMessage());
        }
        userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully!");
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        System.out.println("Login attempt for user: " + user.getEmail());

        Optional<User> existingUser = userService.findByEmail(user.getEmail());
        if (existingUser.isPresent() &&
                passwordEncoder.matches(user.getPassword(), existingUser.get().getPassword())) {

            String token = jwtUtil.generateToken(user.getEmail());
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(Principal principal) {
        String username = principal.getName(); // Get the username from the Principal object

        Optional<User> user = userrepository.findByEmail(username);

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

}