package com.tuition.institute.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUserId(Long userId);
    Optional<Student> findByUserEmail(String email);
    Optional<Student> findByQrToken(String qrToken);
    Optional<Student> findByStudentCode(String studentCode);
}
