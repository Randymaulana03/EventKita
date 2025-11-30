package com.eventkita.controller;

import com.eventkita.dto.AuthRequestDTO;
import com.eventkita.dto.AuthResponseDTO;
import com.eventkita.entity.Organizer;
import com.eventkita.entity.Peserta;
import com.eventkita.entity.User;
import com.eventkita.service.UserService;
import com.eventkita.config.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // ✅ Allow frontend access
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder; // ✅ Inject PasswordEncoder

    public AuthController(UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    // ===========================
    // REGISTER - FIXED VERSION
    // ===========================
    @PostMapping("/register/{role}")
    public ResponseEntity<?> register(@PathVariable String role, @RequestBody AuthRequestDTO request) {
        try {
            System.out.println("🔐 Register attempt for: " + request.getEmail() + " as " + role);

            // Cek duplicate email
            if (userService.existsByEmail(request.getEmail())) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Email already exists");
                return ResponseEntity.badRequest().body(error);
            }

            // Cek duplicate username
            if (userService.existsByUsername(request.getUsername())) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Username already exists");
                return ResponseEntity.badRequest().body(error);
            }

            User user;
            String encodedPassword = passwordEncoder.encode(request.getPassword()); // ✅ ENCODE PASSWORD SEKALI SAJA

            switch(role.toUpperCase()) {
                case "ORGANIZER":
                    if(request.getCompanyName() == null || request.getCompanyName().isBlank()) {
                        Map<String, String> error = new HashMap<>();
                        error.put("error", "Company name is required for Organizer");
                        return ResponseEntity.badRequest().body(error);
                    }
                    user = new Organizer(
                            request.getFullName(),
                            request.getEmail(),
                            request.getUsername(),
                            encodedPassword, // ✅ Pakai encoded password
                            request.getCompanyName()
                    );
                    break;

                default: // PESERTA
                    user = new Peserta(
                            request.getFullName(),
                            request.getEmail(),
                            request.getUsername(),
                            encodedPassword // ✅ Pakai encoded password
                    );
            }

            User savedUser = userService.register(user);
            System.out.println("✅ User registered with ID: " + savedUser.getId());

            // Generate token untuk response
            String token = jwtUtil.generateToken(savedUser.getEmail(), savedUser.getRole());
            
            String companyName = (savedUser instanceof Organizer) ? 
                ((Organizer) savedUser).getCompanyName() : null;

            AuthResponseDTO response = new AuthResponseDTO(
                    savedUser.getId(),
                    savedUser.getFullName(),
                    savedUser.getEmail(),
                    savedUser.getRole().name(),
                    token,
                    companyName
            );

            return ResponseEntity.ok(response);

        } catch(Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Registration failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // ===========================
    // LOGIN - FIXED VERSION
    // ===========================
    @PostMapping("/login/{role}")
    public ResponseEntity<?> login(@PathVariable String role, @RequestBody AuthRequestDTO request) {
        try {
            System.out.println("🔐 Login attempt for: " + request.getEmail());

            User user = userService.login(request.getEmail(), request.getPassword())
                    .orElseThrow(() -> new RuntimeException("Invalid email or password"));

            // Cek role match
            if (!user.getRole().name().equalsIgnoreCase(role)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid role for this login");
                return ResponseEntity.badRequest().body(error);
            }

            // Auto-verify untuk testing (hapus di production)
            if (!user.isVerified()) {
                user.setVerified(true);
                userService.save(user);
                System.out.println("⚠️ Auto-verified user for testing: " + user.getEmail());
            }

            String companyName = (user instanceof Organizer) ? 
                ((Organizer) user).getCompanyName() : null;

            String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

            AuthResponseDTO response = new AuthResponseDTO(
                    user.getId(),
                    user.getFullName(),
                    user.getEmail(),
                    user.getRole().name(),
                    token,
                    companyName
            );

            System.out.println("✅ Login successful for: " + user.getEmail());
            return ResponseEntity.ok(response);

        } catch(Exception e) {
            System.out.println("❌ Login failed: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("service", "EventKita Auth Service");
        return ResponseEntity.ok(response);
    }
}