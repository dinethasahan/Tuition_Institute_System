package com.tuition.institute.academic;

import java.math.BigDecimal;

public class ClassDTO {
    private String id;
    private String name;
    private String subject;
    private String teacherName;
    private String grade;
    private String batch;
    private Integer capacity;
    private Integer enrolledCount;
    private BigDecimal monthlyFee;
    private String hall;
    private String status;

    public ClassDTO() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public String getBatch() { return batch; }
    public void setBatch(String batch) { this.batch = batch; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public Integer getEnrolledCount() { return enrolledCount; }
    public void setEnrolledCount(Integer enrolledCount) { this.enrolledCount = enrolledCount; }

    public BigDecimal getMonthlyFee() { return monthlyFee; }
    public void setMonthlyFee(BigDecimal monthlyFee) { this.monthlyFee = monthlyFee; }

    public String getHall() { return hall; }
    public void setHall(String hall) { this.hall = hall; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
