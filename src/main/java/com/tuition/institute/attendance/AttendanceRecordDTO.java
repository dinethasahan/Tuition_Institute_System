package com.tuition.institute.attendance;

public class AttendanceRecordDTO {
    private String id;
    private String studentId;
    private String studentName;
    private String batch;
    private String subject;
    private String date;
    private String status; // Present, Absent, Late, Excused
    private String markedBy;
    private String time;

    public AttendanceRecordDTO() {}

    public AttendanceRecordDTO(String id, String studentId, String studentName, String batch, String subject, String date, String status, String markedBy, String time) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.batch = batch;
        this.subject = subject;
        this.date = date;
        this.status = status;
        this.markedBy = markedBy;
        this.time = time;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getBatch() { return batch; }
    public void setBatch(String batch) { this.batch = batch; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMarkedBy() { return markedBy; }
    public void setMarkedBy(String markedBy) { this.markedBy = markedBy; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
}
