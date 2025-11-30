package com.eventkita.dto;

import com.eventkita.entity.User;
import com.eventkita.enums.EventCategory;
import com.eventkita.enums.EventStatus;
import com.eventkita.entity.Event;
import java.time.LocalDateTime;
import java.util.List;

public class EventResponseDTO {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String location;
    private String category;
    private String status;
    private Double price;
    private Integer maxParticipants;
    private String tags;
    private String contactInfo;
    private boolean free;
    private boolean full;
    private boolean registrationOpen;
    private LocalDateTime createdAt;
    private OrganizerDTO createdBy;
    private List<?> bookings; // bisa diubah sesuai tipe Booking
    private double komisi;        // baru
    private double revenueBersih; // baru
    private int totalParticipants; // total peserta PAID

    // =========================
    // Constructor utama dari Event
    // =========================
    public EventResponseDTO(Event event, double komisi, double revenueBersih, int totalParticipants) {
        this(event); // panggil constructor lama
        this.komisi = komisi;
        this.revenueBersih = revenueBersih;
        this.totalParticipants = totalParticipants;
    }

    public Event toEntity() {
        Event event = new Event();
        event.setId(this.id);
        event.setTitle(this.title);
        event.setDescription(this.description);
        event.setStartDate(this.startDate);
        event.setEndDate(this.endDate);
        event.setLocation(this.location);

        // Konversi String → EventCategory
        if (this.category != null && !this.category.isEmpty()) {
            try {
                event.setCategory(EventCategory.valueOf(this.category.toUpperCase()));
            } catch (IllegalArgumentException e) {
                event.setCategory(EventCategory.OTHER);
            }
        }

        // Konversi String → EventStatus
        if (this.status != null && !this.status.isEmpty()) {
            try {
                event.setStatus(EventStatus.valueOf(this.status.toUpperCase()));
            } catch (IllegalArgumentException e) {
                event.setStatus(EventStatus.DRAFT);
            }
        }

        event.setPrice(this.price);
        event.setMaxParticipants(this.maxParticipants);
        event.setTags(this.tags);
        event.setContactInfo(this.contactInfo);
        return event;
    }


    public EventResponseDTO(Event event) {
        this.id = event.getId();
        this.title = event.getTitle();
        this.description = event.getDescription();
        this.startDate = event.getStartDate();
        this.endDate = event.getEndDate();
        this.location = event.getLocation();
        this.category = event.getCategory().name();
        this.status = event.getStatus().name();
        this.price = event.getPrice();
        this.maxParticipants = event.getMaxParticipants();
        this.tags = event.getTags();
        this.contactInfo = event.getContactInfo();
        this.createdAt = event.getCreatedAt();
        this.free = event.getPrice() == 0;
        
        // Calculate actual participants from PAID bookings
        int paidParticipants = event.getBookings().stream()
            .filter(b -> b.getStatus() == com.eventkita.enums.BookingStatus.PAID)
            .mapToInt(b -> b.getQuantity())
            .sum();
        this.totalParticipants = paidParticipants;
        this.full = paidParticipants >= event.getMaxParticipants();
        
        this.registrationOpen = event.isRegistrationOpen();
        // Don't include full bookings (causes circular reference) — just count
        this.bookings = null; // exclude booking details to avoid serialization issues

        if (event.getCreatedBy() != null) {
            this.createdBy = new OrganizerDTO(event.getCreatedBy());
        }
        
    }

    // =========================
    // DTO untuk CreatedBy (Organizer)
    // =========================
    public static class OrganizerDTO {
        private Long id;
        private String fullName;
        private String email;
        private String username;
        private String role;
        private String companyName;

        public OrganizerDTO(User user) {
            this.id = user.getId();
            this.fullName = user.getFullName();
            this.email = user.getEmail();
            this.username = user.getUsername();
            this.role = user.getRole().name();
            if (user instanceof com.eventkita.entity.Organizer) {
                this.companyName = ((com.eventkita.entity.Organizer) user).getCompanyName();
            }
        }

        // Getters & Setters
        public Long getId() { return id; }
        public String getFullName() { return fullName; }
        public String getEmail() { return email; }
        public String getUsername() { return username; }
        public String getRole() { return role; }
        public String getCompanyName() { return companyName; }
        
    }

    // =========================
    // Getters & Setters
    // =========================
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDateTime getStartDate() { return startDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public String getLocation() { return location; }
    public String getCategory() { return category; }
    public String getStatus() { return status; }
    public Double getPrice() { return price; }
    public Integer getMaxParticipants() { return maxParticipants; }
    public String getTags() { return tags; }
    public String getContactInfo() { return contactInfo; }
    public boolean isFree() { return free; }
    public boolean isFull() { return full; }
    public boolean isRegistrationOpen() { return registrationOpen; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public OrganizerDTO getCreatedBy() { return createdBy; }
    public List<?> getBookings() { return bookings; }
    public double getKomisi() { return komisi; }
    public void setKomisi(double komisi) { this.komisi = komisi; }

    public double getRevenueBersih() { return revenueBersih; }
    public void setRevenueBersih(double revenueBersih) { this.revenueBersih = revenueBersih; }
    
    public int getTotalParticipants() { return totalParticipants; }
    public void setTotalParticipants(int totalParticipants) { this.totalParticipants = totalParticipants; }
}
