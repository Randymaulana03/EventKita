package com.eventkita.dto;

public class PencairanDanaRequest {
    private Long organizerId;
    private Long eventId;
    private Double amount;

    // Constructors
    public PencairanDanaRequest() {}

    public PencairanDanaRequest(Long organizerId, Long eventId, Double amount) {
        this.organizerId = organizerId;
        this.eventId = eventId;
        this.amount = amount;
    }

    // Getters & Setters
    public Long getOrganizerId() { return organizerId; }
    public void setOrganizerId(Long organizerId) { this.organizerId = organizerId; }
    
    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }
    
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
}