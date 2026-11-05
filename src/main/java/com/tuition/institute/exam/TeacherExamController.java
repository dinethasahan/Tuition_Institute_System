package com.tuition.institute.exam;

import com.tuition.institute.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/teacher/exams")
@PreAuthorize("hasRole('TEACHER')")
public class TeacherExamController {

    private final ExamService examService;

    public TeacherExamController(ExamService examService) {
        this.examService = examService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ExamDTO>>> getTeacherExams(Authentication auth) {
        return ResponseEntity.ok(ApiResponse.ok(examService.getExamsForTeacher(auth.getName())));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ExamDTO>> createExam(@RequestBody ExamDTO examDTO, Authentication auth) {
        ExamDTO created = examService.createExam(examDTO, auth.getName());
        return ResponseEntity.ok(ApiResponse.ok("Exam created successfully", created));
    }

    @PostMapping("/{examId}/results")
    public ResponseEntity<ApiResponse<ExamResultDTO>> recordMark(
            @PathVariable Long examId,
            @RequestBody Map<String, Object> body
    ) {
        Long studentId = Long.parseLong(body.get("studentId").toString());
        Double marks = Double.parseDouble(body.get("marks").toString());
        String feedback = body.getOrDefault("feedback", "").toString();

        ExamResultDTO result = examService.recordStudentMark(examId, studentId, marks, feedback);
        return ResponseEntity.ok(ApiResponse.ok("Marks saved successfully", result));
    }
}
