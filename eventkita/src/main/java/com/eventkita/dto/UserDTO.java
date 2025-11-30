package com.eventkita.dto;

public class UserDTO {
    private Long id;
    private String fullname;
    private String email;
    private String role;
    private String username;

    // Constructors
    public UserDTO() {}

    public UserDTO(Long id, String fullname, String email, String role, String username ) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.role = role;
        this.username = username;
    
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullname; }
    public void getFullName(String fullname) { this.fullname = fullname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    
}