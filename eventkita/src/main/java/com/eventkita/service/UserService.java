package com.eventkita.service;

import com.eventkita.entity.User;
import java.util.Optional;
import java.util.List;



public interface UserService {
    User register(User user);
    User save(User user);
    Optional<User> login(String email, String password);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    List<User> findAll();
}
