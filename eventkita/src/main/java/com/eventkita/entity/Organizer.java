package com.eventkita.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import com.eventkita.enums.Role;

@Entity
@DiscriminatorValue("ORGANIZER")
public class Organizer extends User {

    @Column(nullable = true)
    private String companyName;

    public Organizer() { super(); }

    public Organizer(String fullName, String email, String username, String password, String companyName) {
        super(fullName, email, username, password, Role.ORGANIZER);
        this.companyName = companyName;
    }

    // Getter & Setter
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
}
