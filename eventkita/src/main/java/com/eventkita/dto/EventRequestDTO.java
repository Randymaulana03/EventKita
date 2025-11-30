package com.eventkita.dto;

import com.eventkita.enums.EventCategory;
import com.eventkita.enums.EventStatus;
import java.time.LocalDateTime;
import java.util.List;

public class EventRequestDTO {
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String location;
    private EventCategory category;
    private EventStatus status;
    
    // Tambahan sesuai Entity
    private Double price;
    private Integer maxParticipants;
    private String tags;
    private String contactInfo;
    private Boolean requireApproval;
    
    // Tipe tiket yang dipilih organizer
    private List<TicketTypeRequestDTO> ticketTypes;

    // --- Getter Setter untuk SEMUA field ---
    
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

    // Getter Setter Baru
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
    
    public List<TicketTypeRequestDTO> getTicketTypes() { return ticketTypes; }
    public void setTicketTypes(List<TicketTypeRequestDTO> ticketTypes) { this.ticketTypes = ticketTypes; }
}