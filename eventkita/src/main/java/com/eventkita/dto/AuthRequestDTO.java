package com.eventkita.dto;

public class AuthRequestDTO {
    private String fullName;
    private String username;
    private String email;
    private String password;
    private String companyName;

    public AuthRequestDTO() {}
    public AuthRequestDTO(String fullName, String username, String email, String password, String companyName) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.companyName = companyName;
    }

    // ===== Getters & Setters =====
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCompanyName() { 
        return companyName; 
    }
        
    public void setCompanyName(String companyName) { 
        this.companyName = companyName; 
    }
}
