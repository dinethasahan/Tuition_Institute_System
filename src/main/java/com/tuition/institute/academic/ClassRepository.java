package com.tuition.institute.academic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<ClassEntity, Long> {
    List<ClassEntity> findByTeacherId(Long teacherId);
    List<ClassEntity> findByTeacherUserEmail(String email);
    List<ClassEntity> findByStatus(String status);
}
