package com.eventkita.dto;

public class WithdrawDTO {
    private Long id;
    private Long organizerId;
    private Double amount;
    private String status;
    private String createdAt;

    // Constructors
    public WithdrawDTO() {}

    public WithdrawDTO(Long id, Long organizerId, Double amount, String status, String createdAt) {
        this.id = id;
        this.organizerId = organizerId;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getOrganizerId() { return organizerId; }
    public void setOrganizerId(Long organizerId) { this.organizerId = organizerId; }
    
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}