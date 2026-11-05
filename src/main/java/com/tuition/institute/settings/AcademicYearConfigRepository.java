package com.tuition.institute.settings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AcademicYearConfigRepository extends JpaRepository<AcademicYearConfig, Long> {
    Optional<AcademicYearConfig> findByIsActiveTrue();
}
