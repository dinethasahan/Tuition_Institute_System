package com.tuition.institute.academic;

import com.tuition.institute.user.Teacher;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "classes")
public class ClassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 50)
    private String grade;

    @Column(name = "batch_name", length = 50)
    private String batchName;

    @Column(nullable = false)
    private Integer capacity = 50;

    @Column(name = "monthly_fee", nullable = false, precision = 10, scale = 2)
    private BigDecimal monthlyFee;

    @Column(length = 50)
    private String hall;

    @Column(nullable = false, length = 20)
    private String status = "ACTIVE";

    public ClassEntity() {}

    public ClassEntity(Subject subject, Teacher teacher, String name, String grade, String batchName, Integer capacity, BigDecimal monthlyFee, String hall) {
        this.subject = subject;
        this.teacher = teacher;
        this.name = name;
        this.grade = grade;
        this.batchName = batchName;
        this.capacity = capacity;
        this.monthlyFee = monthlyFee;
        this.hall = hall;
        this.status = "ACTIVE";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }

    public Teacher getTeacher() { return teacher; }
    public void setTeacher(Teacher teacher) { this.teacher = teacher; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public String getBatchName() { return batchName; }
    public void setBatchName(String batchName) { this.batchName = batchName; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public BigDecimal getMonthlyFee() { return monthlyFee; }
    public void setMonthlyFee(BigDecimal monthlyFee) { this.monthlyFee = monthlyFee; }

    public String getHall() { return hall; }
    public void setHall(String hall) { this.hall = hall; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
