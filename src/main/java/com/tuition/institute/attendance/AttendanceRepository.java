package com.tuition.institute.attendance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findBySessionId(Long sessionId);
    List<Attendance> findByStudentId(Long studentId);
    List<Attendance> findByStudentUserEmail(String email);
    Optional<Attendance> findBySessionIdAndStudentId(Long sessionId, Long studentId);
}
