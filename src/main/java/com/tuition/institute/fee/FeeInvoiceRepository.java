package com.tuition.institute.fee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeeInvoiceRepository extends JpaRepository<FeeInvoice, Long> {
    List<FeeInvoice> findByStudentId(Long studentId);
    List<FeeInvoice> findByStatus(String status);
    Optional<FeeInvoice> findByInvoiceNumber(String invoiceNumber);
}
