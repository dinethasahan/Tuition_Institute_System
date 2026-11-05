package com.tuition.institute.academic;

import com.tuition.institute.user.Student;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "enrollments")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false)
    private ClassEntity classEntity;

    @Column(name = "enrolled_on", nullable = false)
    private LocalDate enrolledOn = LocalDate.now();

    @Column(nullable = false, length = 20)
    private String status = "ACTIVE"; // ACTIVE, LEFT, SUSPENDED

    public Enrollment() {}

    public Enrollment(Student student, ClassEntity classEntity, LocalDate enrolledOn, String status) {
        this.student = student;
        this.classEntity = classEntity;
        this.enrolledOn = enrolledOn;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public ClassEntity getClassEntity() { return classEntity; }
    public void setClassEntity(ClassEntity classEntity) { this.classEntity = classEntity; }

    public LocalDate getEnrolledOn() { return enrolledOn; }
    public void setEnrolledOn(LocalDate enrolledOn) { this.enrolledOn = enrolledOn; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
