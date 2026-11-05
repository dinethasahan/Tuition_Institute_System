package com.tuition.institute.exam;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findByClassEntityId(Long classId);
    List<Exam> findByClassEntityTeacherUserEmail(String email);
}
