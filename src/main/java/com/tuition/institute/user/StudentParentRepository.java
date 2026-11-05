package com.tuition.institute.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentParentRepository extends JpaRepository<StudentParent, StudentParentId> {
    List<StudentParent> findByParentId(Long parentId);
    List<StudentParent> findByStudentId(Long studentId);
    boolean existsByParentIdAndStudentId(Long parentId, Long studentId);
    Optional<StudentParent> findByStudentIdAndIsPrimaryTrue(Long studentId);
}
