package com.eventkita.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import com.eventkita.enums.Role;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User {
    public Admin() { super(); }
    public Admin(String fullName, String email, String username, String password) {
        super(fullName, email, username, password, Role.ADMIN);
    }
}