package com.eventkita.service;

import com.eventkita.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService; // inject interface UserService

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOpt = userService.findByEmail(email);
        User user = userOpt.orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // pakai class CustomUserDetails yang sudah dibuat di file terpisah
        return new CustomUserDetails(user.getEmail(), user.getPassword(), user.getRole());
    }
}
