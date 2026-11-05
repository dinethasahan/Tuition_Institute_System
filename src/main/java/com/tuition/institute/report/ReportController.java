package com.tuition.institute.report;

import com.tuition.institute.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ReportController {

    private final DashboardService dashboardService;

    public ReportController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAdminDashboard() {
        return ResponseEntity.ok(ApiResponse.ok(dashboardService.getAdminDashboardMetrics()));
    }

    @GetMapping("/teacher/dashboard")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getTeacherDashboard() {
        Map<String, Object> data = new HashMap<>();
        data.put("classesToday", 3);
        data.put("marksPending", "2 exams");
        data.put("avgAttendance", "91%");
        data.put("unreadMessages", 4);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    @GetMapping("/student/dashboard")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStudentDashboard() {
        Map<String, Object> data = new HashMap<>();
        data.put("attendanceRate", 94.2);
        data.put("nextExam", "2026-11-12");
        data.put("newNotes", 3);
        data.put("latestResult", "78%");
        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    @GetMapping("/parent/dashboard")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getParentDashboard(Authentication auth) {
        List<Map<String, Object>> children = dashboardService.getParentChildrenSummary(auth.getName());
        Map<String, Object> data = new HashMap<>();
        data.put("children", children);
        data.put("selectedChild", children.isEmpty() ? null : children.get(0));
        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    @GetMapping("/parent/children")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getParentChildren(Authentication auth) {
        return ResponseEntity.ok(ApiResponse.ok(dashboardService.getParentChildrenSummary(auth.getName())));
    }
}
