package com.eventkita.entity;

import com.eventkita.enums.EventCategory;
import com.eventkita.enums.EventStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    private EventCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventStatus status = EventStatus.DRAFT;

    // --- FIELD BARU (Sesuai Frontend) ---
    
    @Column(name = "price")
    private Double price = 0.0; // Default 0 (Gratis)

    @Column(name = "max_participants")
    private Integer maxParticipants; // Jika null atau 0, anggap unlimited

    @Column(name = "tags")
    private String tags; // Disimpan sebagai string koma (contoh: "tech,java,coding")

    @Column(name = "contact_info")
    private String contactInfo;

    @Column(name = "require_approval")
    private Boolean requireApproval = false; // Default auto-approve

    // ------------------------------------

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventTicketType> availableTicketTypes = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Event() {
    }

    // Constructor Praktis
    public Event(String title, String description, LocalDateTime startDate, LocalDateTime endDate, 
                 String location, EventCategory category, User createdBy) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.category = category;
        this.createdBy = createdBy;
    }

    // === Business Logic Update ===
    
    public boolean isFree() {
        return this.price == null || this.price == 0.0;
    }

    public boolean isFull() {
        if (this.maxParticipants == null || this.maxParticipants == 0) {
            return false; // Unlimited
        }
        return bookings.size() >= this.maxParticipants;
    }

    public boolean isRegistrationOpen() {
        return this.status == EventStatus.PUBLISHED 
            && LocalDateTime.now().isBefore(startDate)
            && !isFull();
    }

    // === Getters & Setters (LENGKAP) ===

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public EventCategory getCategory() { return category; }
    public void setCategory(EventCategory category) { this.category = category; }

    public EventStatus getStatus() { return status; }
    public void setStatus(EventStatus status) { this.status = status; }

    // Getter Setter Field Baru
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(Integer maxParticipants) { this.maxParticipants = maxParticipants; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }

    public Boolean getRequireApproval() { return requireApproval; }
    public void setRequireApproval(Boolean requireApproval) { this.requireApproval = requireApproval; }

    // Getter Setter Relasi
    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }

    public List<Booking> getBookings() { return bookings; }
    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }

    public List<EventTicketType> getAvailableTicketTypes() { return availableTicketTypes; }
    public void setAvailableTicketTypes(List<EventTicketType> availableTicketTypes) { this.availableTicketTypes = availableTicketTypes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}