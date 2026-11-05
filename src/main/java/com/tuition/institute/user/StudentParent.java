package com.tuition.institute.user;

import jakarta.persistence.*;

@Entity
@Table(name = "student_parents")
@IdClass(StudentParentId.class)
public class StudentParent {

    @Id
    @Column(name = "student_id")
    private Long studentId;

    @Id
    @Column(name = "parent_id")
    private Long parentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", insertable = false, updatable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    private Parent parent;

    @Column(nullable = false, length = 20)
    private String relationship = "GUARDIAN";

    @Column(name = "is_primary", nullable = false)
    private boolean isPrimary = false;

    public StudentParent() {}

    public StudentParent(Student student, Parent parent, String relationship, boolean isPrimary) {
        this.student = student;
        this.parent = parent;
        this.studentId = student.getId();
        this.parentId = parent.getId();
        this.relationship = relationship;
        this.isPrimary = isPrimary;
    }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Parent getParent() { return parent; }
    public void setParent(Parent parent) { this.parent = parent; }

    public String getRelationship() { return relationship; }
    public void setRelationship(String relationship) { this.relationship = relationship; }

    public boolean isPrimary() { return isPrimary; }
    public void setPrimary(boolean primary) { isPrimary = primary; }
}
