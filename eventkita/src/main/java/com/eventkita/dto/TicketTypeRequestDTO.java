package com.eventkita.dto;

import com.eventkita.enums.TicketType;
import lombok.*;

/**
 * DTO untuk menerima input tipe tiket dari frontend
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketTypeRequestDTO {
    private TicketType ticketType;
    private Double price;
    private Integer quota;
}
