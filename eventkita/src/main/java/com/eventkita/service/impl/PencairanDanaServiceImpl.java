package com.eventkita.service.impl;

import com.eventkita.entity.PencairanDana;
import com.eventkita.enums.StatusPencairan;
import com.eventkita.repository.PencairanDanaRepository;
import com.eventkita.service.PencairanDanaService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PencairanDanaServiceImpl implements PencairanDanaService {
    
    private final PencairanDanaRepository repository;
    
    public PencairanDanaServiceImpl(PencairanDanaRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public PencairanDana createPencairan(PencairanDana pencairan) {
        return repository.save(pencairan);
    }
    
    @Override
    public List<PencairanDana> getOrganizerPencairan(Long organizerId) {
        return repository.findByOrganizer_Id(organizerId);
    }
    
    @Override
    public PencairanDana updateStatus(Long pencairanId, StatusPencairan status) {
        PencairanDana pencairan = repository.findById(pencairanId)
            .orElseThrow(() -> new RuntimeException("Pencairan not found"));
        
        pencairan.setStatus(status);
        if (status == StatusPencairan.COMPLETED) {
            pencairan.markAsCompleted();
        }
        return repository.save(pencairan);
    }
    
    @Override
    public List<PencairanDana> getByStatus(StatusPencairan status) {
        return repository.findByStatus(status);
    }
}