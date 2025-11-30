package com.eventkita.controller;

import com.eventkita.entity.User;
import com.eventkita.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return service.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
