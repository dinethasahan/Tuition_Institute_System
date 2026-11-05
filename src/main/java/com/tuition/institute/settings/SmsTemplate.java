package com.tuition.institute.settings;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "sms_templates")
public class SmsTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 50)
    private String type; // Fee Alert, Attendance, Exam Results, General Notice

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(length = 255)
    private String variables;

    @Column(name = "last_updated", nullable = false)
    private LocalDate lastUpdated = LocalDate.now();

    public SmsTemplate() {}

    public SmsTemplate(String title, String type, String content, String variables) {
        this.title = title;
        this.type = type;
        this.content = content;
        this.variables = variables;
        this.lastUpdated = LocalDate.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getVariables() { return variables; }
    public void setVariables(String variables) { this.variables = variables; }

    public LocalDate getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDate lastUpdated) { this.lastUpdated = lastUpdated; }
}
