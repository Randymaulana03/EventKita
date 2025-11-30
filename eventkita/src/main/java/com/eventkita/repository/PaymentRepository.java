package com.eventkita.repository;

import com.eventkita.entity.Payment;
import com.eventkita.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByReferenceId(String referenceId);
    List<Payment> findByStatus(PaymentStatus status);
    Optional<Payment> findByBooking_Id(Long bookingId);
}