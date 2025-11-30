package com.eventkita.dto;

import com.eventkita.enums.TicketType;
import lombok.*;

/**
 * DTO untuk request booking dari frontend
 * User memilih event dan tipe tiket yang ingin dibeli
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequestDTO {
    private Long eventId;
    private TicketType ticketType;  // Pilihan user: REGULAR, VIP, atau VVIP
    private Integer quantity;        // Jumlah tiket yang dibeli
}
