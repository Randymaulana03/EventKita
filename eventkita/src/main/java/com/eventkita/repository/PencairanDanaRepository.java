// Repository - ganti PayoutStatus → StatusPencairan
package com.eventkita.repository;

import com.eventkita.entity.PencairanDana;
import com.eventkita.enums.StatusPencairan; // ✅ PAKAI INI
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PencairanDanaRepository extends JpaRepository<PencairanDana, Long> {
    List<PencairanDana> findByOrganizer_Id(Long organizerId);
    List<PencairanDana> findByStatus(StatusPencairan status); // ✅ FIX
    List<PencairanDana> findByEvent_Id(Long eventId);
}