package com.eventkita.dto;

import java.time.LocalDateTime;

public class BookingDTO {
    private Long userId;
    private Long eventId;
    private Long ticketId;
    private Integer quantity;
    private Double totalAmount;
    private LocalDateTime createdAt;
    


    // Constructors
    public BookingDTO() {}
    
    public BookingDTO(Long userId, Long eventId, Long ticketId, Integer quantity, Double totalAmount, LocalDateTime createdAt) {
        this.userId = userId;
        this.eventId = eventId;
        this.ticketId = ticketId;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
    }

    // Getters & Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }
    
    public Long getTicketId() { return ticketId; }
    public void setTicketId(Long ticketId) { this.ticketId = ticketId; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

}