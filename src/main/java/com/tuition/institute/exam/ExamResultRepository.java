package com.tuition.institute.exam;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamResultRepository extends JpaRepository<ExamResult, Long> {
    List<ExamResult> findByExamId(Long examId);
    List<ExamResult> findByStudentId(Long studentId);
    List<ExamResult> findByStudentUserEmail(String email);
    Optional<ExamResult> findByExamIdAndStudentId(Long examId, Long studentId);
}
