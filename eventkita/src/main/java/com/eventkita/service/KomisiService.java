package com.eventkita.service;

import com.eventkita.entity.Komisi;

import java.util.List;
import java.util.Optional;

public interface KomisiService {
    
    Komisi createKomisi(Komisi komisi);
    
    Optional<Komisi> getKomisiById(Long id);
    
    Optional<Komisi> getKomisiByEvent(Long eventId);
    
    List<Komisi> getAllKomisi();
    
    Komisi updateKomisi(Long id, Komisi komisi);
    
    void deleteKomisi(Long id);
    
    /**
     * Calculate komisi untuk event
     */
    double calculateKomisi(Long eventId, double totalAmount);
    
    /**
     * Get komisi untuk organizer berdasarkan periode
     */
    List<Komisi> getOrganizerKomisi(Long organizerId);
}
