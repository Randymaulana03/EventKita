package com.eventkita.service.impl;

import com.eventkita.entity.User;
import com.eventkita.repository.UserRepository;
import com.eventkita.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.eventkita.util.EmailUtils;

import java.util.Optional;
import java.util.Random;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailUtils emailUtils;

    public UserServiceImpl(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       EmailUtils emailUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailUtils = emailUtils;
    }

    @Override
    public User register(User user) {
        System.out.println("💾 Saving user to database: " + user.getEmail());
        
        // ✅ TIDAK ADA PASSWORD ENCODING DI SINI - Sudah dilakukan di AuthController
        // ✅ Hanya set verification code dan save
        
        String code = String.valueOf(new Random().nextInt(900000) + 100000);
        user.setVerifyCode(code);
        user.setVerified(true);  // Auto-verify untuk testing

        User saved = userRepository.save(user);
        System.out.println("✅ User saved with ID: " + saved.getId());
        return saved;
    }

    @Override
    public Optional<User> login(String email, String password) {
        System.out.println("🔐 Login check for: " + email);
        
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            System.out.println("❌ User not found: " + email);
            return Optional.empty();
        }

        User user = userOpt.get();
        System.out.println("🔍 User found, checking password...");

        // ✅ Password comparison dengan encoded password
        if (passwordEncoder.matches(password, user.getPassword())) {
            System.out.println("✅ Password matched for: " + email);
            return userOpt;
        }

        System.out.println("❌ Password mismatch for: " + email);
        return Optional.empty();
    }


    
    @Override
    public Optional<User> findById(Long id) { 
        return userRepository.findById(id); 
    }

    @Override
    public Optional<User> findByEmail(String email) { 
        return userRepository.findByEmail(email); 
    }

    @Override
    public Optional<User> findByUsername(String username) { 
        return userRepository.findByUsername(username); 
    }

    @Override
    public boolean existsByEmail(String email) { 
        return userRepository.existsByEmail(email); 
    }

    @Override
    public boolean existsByUsername(String username) { 
        return userRepository.existsByUsername(username); 
    }
    
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}