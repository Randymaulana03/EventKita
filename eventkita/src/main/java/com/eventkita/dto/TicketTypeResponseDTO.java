package com.eventkita.dto;

import com.eventkita.entity.EventTicketType;
import com.eventkita.enums.TicketType;
import lombok.*;

/**
 * DTO untuk mengirim informasi tipe tiket ke frontend
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketTypeResponseDTO {
    private Long id;
    private TicketType ticketType;
    private Double price;
    private Integer quota;
    private Integer availableQuota;
    
    public TicketTypeResponseDTO(EventTicketType eventTicketType) {
        this.id = eventTicketType.getId();
        this.ticketType = eventTicketType.getTicketType();
        this.price = eventTicketType.getPrice();
        this.quota = eventTicketType.getQuota();
        this.availableQuota = eventTicketType.getAvailableQuota();
    }
}
