package com.eventkita.service.impl;

import com.eventkita.entity.Komisi;
import com.eventkita.repository.KomisiRepository;
import com.eventkita.service.KomisiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class KomisiServiceImpl implements KomisiService {

    @Autowired
    private KomisiRepository komisiRepository;

    @Override
    public Komisi createKomisi(Komisi komisi) {
        return komisiRepository.save(komisi);
    }

    @Override
    public Optional<Komisi> getKomisiById(Long id) {
        return komisiRepository.findById(id);
    }

    @Override
    public List<Komisi> getAllKomisi() {
        return komisiRepository.findAll();
    }

    @Override
    public Komisi updateKomisi(Long id, Komisi komisiDetails) {
        Optional<Komisi> komisi = komisiRepository.findById(id);
        if (komisi.isPresent()) {
            Komisi k = komisi.get();
            if (komisiDetails.getPersentase() != null) {
                k.setPersentase(komisiDetails.getPersentase());
            }
            if (komisiDetails.getAmount() != null) {
                k.setAmount(komisiDetails.getAmount());
            }
            if (komisiDetails.getBooking() != null) {
                k.setBooking(komisiDetails.getBooking());
            }
            if (komisiDetails.getEvent() != null) {
                k.setEvent(komisiDetails.getEvent());
            }
            return komisiRepository.save(k);
        }
        throw new IllegalArgumentException("Komisi not found with id: " + id);
    }

    @Override
    public void deleteKomisi(Long id) {
        komisiRepository.deleteById(id);
    }

    @Override
    public double calculateKomisi(Long eventId, double totalAmount) {
        // Default komisi percentage
        double defaultPersentase = 10.0; // 10%
        // If there are existing komisi records for the event, use the first persentase as reference
        try {
            var list = komisiRepository.findByEvent_Id(eventId);
            if (list != null && !list.isEmpty()) {
                Double p = list.get(0).getPersentase();
                if (p != null) defaultPersentase = p;
            }
        } catch (Exception ignored) {}

        return Komisi.calculateCommission(totalAmount, defaultPersentase);
    }

    @Override
    public List<Komisi> getOrganizerKomisi(Long organizerId) {
        return komisiRepository.findByEvent_CreatedBy_Id(organizerId);
    }
    @Override
    public Optional<Komisi> getKomisiByEvent(Long eventId) {
        List<Komisi> list = komisiRepository.findByEvent_Id(eventId);
        if (list != null && !list.isEmpty()) return Optional.of(list.get(0));
        return Optional.empty();
    }
}
