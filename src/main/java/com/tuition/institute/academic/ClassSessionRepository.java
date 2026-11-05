package com.tuition.institute.academic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClassSessionRepository extends JpaRepository<ClassSession, Long> {
    List<ClassSession> findBySessionDate(LocalDate date);
    List<ClassSession> findByClassEntityId(Long classId);
    List<ClassSession> findByClassEntityTeacherUserEmailAndSessionDate(String email, LocalDate date);
    List<ClassSession> findByClassEntityTeacherUserEmail(String email);
}
