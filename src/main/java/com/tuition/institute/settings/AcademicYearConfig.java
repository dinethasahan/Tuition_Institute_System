package com.tuition.institute.settings;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "academic_year_configs")
public class AcademicYearConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "current_term", nullable = false, length = 100)
    private String currentTerm;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = false;

    public AcademicYearConfig() {}

    public AcademicYearConfig(String name, LocalDate startDate, LocalDate endDate, String currentTerm, boolean isActive) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.currentTerm = currentTerm;
        this.isActive = isActive;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getCurrentTerm() { return currentTerm; }
    public void setCurrentTerm(String currentTerm) { this.currentTerm = currentTerm; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}
