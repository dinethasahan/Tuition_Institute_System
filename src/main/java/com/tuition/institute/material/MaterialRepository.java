package com.tuition.institute.material;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    List<Material> findByClassEntityId(Long classId);
    List<Material> findByClassEntityTeacherUserEmail(String email);
}
