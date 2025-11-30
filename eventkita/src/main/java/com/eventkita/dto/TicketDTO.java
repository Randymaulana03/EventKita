package com.eventkita.dto;

public class TicketDTO {
    private Long eventId;
    private String type;        // ✅ Ganti ticketType → type
    private Double price;       // ✅ double → Double
    private Integer quota;      // ✅ Ganti stock → quota

    // Constructors
    public TicketDTO() {}
    
    public TicketDTO(Long eventId, String type, Double price, Integer quota) {
        this.eventId = eventId;
        this.type = type;
        this.price = price;
        this.quota = quota;
    }

    // Getters & Setters
    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    
    public Integer getQuota() { return quota; }
    public void setQuota(Integer quota) { this.quota = quota; }
}