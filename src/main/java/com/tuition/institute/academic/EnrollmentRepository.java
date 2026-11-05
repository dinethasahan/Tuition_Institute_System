package com.tuition.institute.academic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByClassEntityId(Long classId);
    List<Enrollment> findByStudentId(Long studentId);
    List<Enrollment> findByStudentUserEmail(String email);
    Optional<Enrollment> findByStudentIdAndClassEntityId(Long studentId, Long classId);
    boolean existsByStudentIdAndClassEntityIdAndStatus(Long studentId, Long classId, String status);
}
