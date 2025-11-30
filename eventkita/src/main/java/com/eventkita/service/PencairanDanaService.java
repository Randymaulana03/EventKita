package com.eventkita.service;

import com.eventkita.entity.PencairanDana;
import com.eventkita.enums.StatusPencairan;
import java.util.List;

public interface PencairanDanaService {
    PencairanDana createPencairan(PencairanDana pencairan);
    List<PencairanDana> getOrganizerPencairan(Long organizerId);
    PencairanDana updateStatus(Long pencairanId, StatusPencairan status);
    List<PencairanDana> getByStatus(StatusPencairan status);
}