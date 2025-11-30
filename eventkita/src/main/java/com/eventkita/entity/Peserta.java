package com.eventkita.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import com.eventkita.enums.Role;

@Entity
@DiscriminatorValue("PESERTA") // ← HARUS SAMA dengan enum value
public class Peserta extends User {
    public Peserta() { super(); }
    
    public Peserta(String fullName, String email, String username, String password) {
        super(fullName, email, username, password, Role.PESERTA); // Role.PESERTA = "PESERTA"
    }
}