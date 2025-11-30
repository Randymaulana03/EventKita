package com.eventkita.controller;

import com.eventkita.dto.PencairanDanaRequest;
import com.eventkita.dto.WithdrawDTO;
import com.eventkita.entity.PencairanDana;
import com.eventkita.enums.StatusPencairan;
import com.eventkita.service.PencairanDanaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/withdraws")
public class PencairanDanaController {

    private final PencairanDanaService service;

    public PencairanDanaController(PencairanDanaService service) {
        this.service = service;
    }

    @PostMapping
    public WithdrawDTO requestWithdraw(@RequestBody PencairanDanaRequest request) {
        // TODO: Implement logic untuk create pencairan dana
        // Untuk sekarang return dummy response
        return new WithdrawDTO(1L, request.getOrganizerId(), request.getAmount(), "PENDING", "2024-01-01");
    }

    @GetMapping("/organizer/{organizerId}")
    public List<WithdrawDTO> getOrganizerWithdraws(@PathVariable Long organizerId) {
        List<PencairanDana> pencairanList = service.getOrganizerPencairan(organizerId);
        
        return pencairanList.stream()
                .map(p -> new WithdrawDTO(
                    p.getId(),
                    p.getOrganizer().getId(),
                    p.getAmount(),
                    p.getStatus().name(),
                    p.getCreatedAt().toString()
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/status/{status}")
    public List<WithdrawDTO> getWithdrawsByStatus(@PathVariable String status) {
        StatusPencairan statusEnum = StatusPencairan.valueOf(status.toUpperCase());
        List<PencairanDana> pencairanList = service.getByStatus(statusEnum);
        
        return pencairanList.stream()
                .map(p -> new WithdrawDTO(
                    p.getId(),
                    p.getOrganizer().getId(),
                    p.getAmount(),
                    p.getStatus().name(),
                    p.getCreatedAt().toString()
                ))
                .collect(Collectors.toList());
    }

    @PostMapping("/{id}/status")
    public WithdrawDTO updateWithdrawStatus(@PathVariable Long id, @RequestParam String status) {
        StatusPencairan statusEnum = StatusPencairan.valueOf(status.toUpperCase());
        PencairanDana updated = service.updateStatus(id, statusEnum);
        
        return new WithdrawDTO(
            updated.getId(),
            updated.getOrganizer().getId(),
            updated.getAmount(),
            updated.getStatus().name(),
            updated.getCreatedAt().toString()
        );
    }
}