package com.tuition.institute.settings;

import com.tuition.institute.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
public class SettingsController {

    private final AcademicYearConfigRepository academicYearRepo;
    private final GradingScaleRepository gradingScaleRepo;
    private final SmsTemplateRepository smsTemplateRepo;
    private final AuditLogRepository auditLogRepo;

    public SettingsController(
            AcademicYearConfigRepository academicYearRepo,
            GradingScaleRepository gradingScaleRepo,
            SmsTemplateRepository smsTemplateRepo,
            AuditLogRepository auditLogRepo
    ) {
        this.academicYearRepo = academicYearRepo;
        this.gradingScaleRepo = gradingScaleRepo;
        this.smsTemplateRepo = smsTemplateRepo;
        this.auditLogRepo = auditLogRepo;
    }

    @GetMapping("/settings/academic-year")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getAcademicYears() {
        List<Map<String, Object>> list = academicYearRepo.findAll().stream().map(a -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", "ay-" + a.getId());
            map.put("name", a.getName());
            map.put("startDate", a.getStartDate().toString());
            map.put("endDate", a.getEndDate().toString());
            map.put("currentTerm", a.getCurrentTerm());
            map.put("isActive", a.isActive());
            return map;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @GetMapping("/settings/grading-scale")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getGradingScales() {
        List<Map<String, Object>> list = gradingScaleRepo.findAllByOrderByMinPercentageDesc().stream().map(g -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", "gr-" + g.getId());
            map.put("grade", g.getGrade());
            map.put("minPercentage", g.getMinPercentage());
            map.put("maxPercentage", g.getMaxPercentage());
            map.put("gradePoint", g.getGradePoint());
            map.put("description", g.getDescription());
            return map;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @GetMapping("/settings/sms-templates")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getSmsTemplates() {
        List<Map<String, Object>> list = smsTemplateRepo.findAll().stream().map(s -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", "sms-" + s.getId());
            map.put("title", s.getTitle());
            map.put("type", s.getType());
            map.put("content", s.getContent());
            map.put("variables", s.getVariables() != null ? Arrays.asList(s.getVariables().split(",")) : Collections.emptyList());
            map.put("lastUpdated", s.getLastUpdated().toString());
            return map;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @GetMapping("/audit-logs")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getAuditLogs() {
        List<Map<String, Object>> list = auditLogRepo.findAllByOrderByCreatedAtDesc().stream().map(l -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", "log-" + l.getId());
            map.put("timestamp", l.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            map.put("user", l.getUser() != null ? l.getUser().getFullName() : "System");
            map.put("userRole", l.getUserRole() != null ? l.getUserRole() : "Automated");
            map.put("action", l.getAction());
            map.put("category", l.getCategory() != null ? l.getCategory() : "Security");
            map.put("ipAddress", l.getIpAddress() != null ? l.getIpAddress() : "127.0.0.1");
            map.put("status", l.getStatus() != null ? l.getStatus() : "Success");
            return map;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok(list));
    }
}
