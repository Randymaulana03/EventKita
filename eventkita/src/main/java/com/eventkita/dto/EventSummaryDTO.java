package com.eventkita.dto;

import com.eventkita.entity.Event;
import com.eventkita.enums.EventStatus;
import java.time.LocalDateTime;

public class EventSummaryDTO {
    
    private Long id;
    private String title; // Nama event
    private String description;
    private LocalDateTime startDate;
    private String location; // Menggantikan 'city' di frontend
    private EventStatus status;
    private Double price;
    private Integer maxParticipants;
    private Integer currentParticipants; // Field yang dihitung dari PAID bookings
    private Double revenueBersih;        // Revenue bersih setelah komisi
    private Double komisi;               // Komisi platform

    public EventSummaryDTO(Event event) {
        this.id = event.getId();
        this.title = event.getTitle();
        this.description = event.getDescription();
        this.startDate = event.getStartDate();
        this.location = event.getLocation();
        this.status = event.getStatus();
        this.price = event.getPrice();
        this.maxParticipants = event.getMaxParticipants();
        
        // Hitung peserta dari PAID bookings dengan quantity
        this.currentParticipants = event.getBookings() != null 
            ? event.getBookings().stream()
                .filter(b -> b.getStatus() == com.eventkita.enums.BookingStatus.PAID)
                .mapToInt(b -> b.getQuantity())
                .sum()
            : 0;
        
        // Hitung revenue dan komisi dari PAID bookings
        double totalRevenue = event.getBookings() != null
            ? event.getBookings().stream()
                .filter(b -> b.getStatus() == com.eventkita.enums.BookingStatus.PAID)
                .mapToDouble(b -> b.getTotalAmount())
                .sum()
            : 0;
        
        this.komisi = totalRevenue * 0.1;
        this.revenueBersih = totalRevenue - this.komisi;
    }

    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDateTime getStartDate() { return startDate; }
    public String getLocation() { return location; }
    public EventStatus getStatus() { return status; }
    public Double getPrice() { return price; }
    public Integer getMaxParticipants() { return maxParticipants; }
    public Integer getCurrentParticipants() { return currentParticipants; }
    public Double getRevenueBersih() { return revenueBersih; }
    public Double getKomisi() { return komisi; }

    // Setters (Opsional untuk DTO)
    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    public void setLocation(String location) { this.location = location; }
    public void setStatus(EventStatus status) { this.status = status; }
    public void setPrice(Double price) { this.price = price; }
    public void setMaxParticipants(Integer maxParticipants) { this.maxParticipants = maxParticipants; }
    public void setCurrentParticipants(Integer currentParticipants) { this.currentParticipants = currentParticipants; }
    public void setRevenueBersih(Double revenueBersih) { this.revenueBersih = revenueBersih; }
    public void setKomisi(Double komisi) { this.komisi = komisi; }
}