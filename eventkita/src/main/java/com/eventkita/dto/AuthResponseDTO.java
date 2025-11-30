package com.eventkita.dto;

public class AuthResponseDTO {
     private Long userId;
    private String fullName;
    private String email;
    private String role;
    private String token;
    private String companyName;

    // Constructors
    public AuthResponseDTO() {}
    public AuthResponseDTO(Long userId, String fullName, String email, String role, String token, String companyName) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.token = token;
        this.companyName = companyName;
    }

    // Getters & Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getName() { return fullName; }
    public void setName(String name) { this.fullName = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
}
