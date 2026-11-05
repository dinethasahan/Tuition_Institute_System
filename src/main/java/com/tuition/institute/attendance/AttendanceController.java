package com.tuition.institute.attendance;

import com.tuition.institute.common.ApiResponse;
import com.tuition.institute.common.ResourceNotFoundException;
import com.tuition.institute.user.Student;
import com.tuition.institute.user.StudentRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final StudentRepository studentRepository;

    public AttendanceController(AttendanceService attendanceService, StudentRepository studentRepository) {
        this.attendanceService = attendanceService;
        this.studentRepository = studentRepository;
    }

    // Teacher QR scan
    @PostMapping("/teacher/attendance/sessions/{sessionId}/scan")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<AttendanceRecordDTO>> scanQr(
            @PathVariable Long sessionId,
            @Valid @RequestBody ScanRequest request,
            Authentication auth
    ) {
        AttendanceRecordDTO result = attendanceService.recordQrScan(sessionId, request.getQrToken(), auth.getName());
        return ResponseEntity.ok(ApiResponse.ok("Attendance scanned successfully", result));
    }

    // Teacher save manual batch attendance
    @PostMapping("/teacher/attendance/sessions/{sessionId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> saveBatch(
            @PathVariable Long sessionId,
            @RequestBody List<AttendanceRecordDTO> records,
            Authentication auth
    ) {
        int count = attendanceService.saveBatchAttendance(sessionId, records, auth.getName());
        Map<String, Object> resp = new HashMap<>();
        resp.put("updatedCount", count);
        resp.put("sessionId", sessionId);
        return ResponseEntity.ok(ApiResponse.ok("Batch attendance saved", resp));
    }

    // Student view own attendance
    @GetMapping("/student/attendance")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<List<AttendanceRecordDTO>>> getStudentAttendance(Authentication auth) {
        List<AttendanceRecordDTO> records = attendanceService.getAttendanceForStudent(auth.getName());
        return ResponseEntity.ok(ApiResponse.ok(records));
    }

    // Student retrieve digital ID and QR pass details
    @GetMapping("/student/qr")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStudentQrPass(Authentication auth) {
        Student student = studentRepository.findByUserEmail(auth.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found"));

        Map<String, Object> data = new HashMap<>();
        data.put("studentId", student.getId());
        data.put("studentCode", student.getStudentCode());
        data.put("qrToken", student.getQrToken());
        data.put("studentName", student.getUser().getFullName());
        data.put("grade", student.getGrade());
        data.put("avatar", student.getUser().getAvatarUrl());

        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    // Parent view child attendance
    @GetMapping("/parent/children/{childId}/attendance")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<ApiResponse<List<AttendanceRecordDTO>>> getChildAttendance(
            @PathVariable Long childId,
            Authentication auth
    ) {
        List<AttendanceRecordDTO> records = attendanceService.getAttendanceForParentChild(auth.getName(), childId);
        return ResponseEntity.ok(ApiResponse.ok(records));
    }
}
