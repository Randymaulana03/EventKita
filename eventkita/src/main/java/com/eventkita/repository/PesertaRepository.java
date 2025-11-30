package com.eventkita.repository;

import com.eventkita.entity.Peserta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PesertaRepository extends JpaRepository<Peserta, Long> {
    // Inherits all methods from UserRepository automatically
}