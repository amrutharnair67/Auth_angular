package com.example.Authdemo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be in a valid format")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "username cannot be blank")
    @Pattern(regexp = "^[A-Za-z]+$", message = " username must contain only letters.")
    private String username;

    @Column(nullable = false)
    @Size(min = 8, message = "Password must contain at least 8 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",message = "Password must contain at least one uppercase letter, one lowercase letter, and one special character.")
    private String password;

    @NotBlank(message = "Role is required")
    private String roles;
}