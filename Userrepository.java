package com.example.Authdemo.repository;


import com.example.Authdemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Userrepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

}
