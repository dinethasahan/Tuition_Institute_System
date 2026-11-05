package com.tuition.institute.exam;

import com.tuition.institute.common.ApiResponse;
import com.tuition.institute.common.ResourceNotFoundException;
import com.tuition.institute.user.Student;
import com.tuition.institute.user.StudentRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class StudentExamController {

    private final ExamService examService;
    private final ExamResultRepository examResultRepository;
    private final StudentRepository studentRepository;
    private final ReportCardService reportCardService;

    public StudentExamController(
            ExamService examService,
            ExamResultRepository examResultRepository,
            StudentRepository studentRepository,
            ReportCardService reportCardService
    ) {
        this.examService = examService;
        this.examResultRepository = examResultRepository;
        this.studentRepository = studentRepository;
        this.reportCardService = reportCardService;
    }

    @GetMapping("/student/results")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<List<ExamResultDTO>>> getStudentResults(Authentication auth) {
        List<ExamResultDTO> results = examService.getResultsForStudent(auth.getName());
        return ResponseEntity.ok(ApiResponse.ok(results));
    }

    @GetMapping("/student/report-card")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<byte[]> downloadStudentReportCard(Authentication auth) {
        Student student = studentRepository.findByUserEmail(auth.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found"));
        List<ExamResult> results = examResultRepository.findByStudentId(student.getId());

        byte[] pdfBytes = reportCardService.generateReportCardPdf(student, results);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ReportCard_" + student.getStudentCode() + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @GetMapping("/parent/children/{childId}/results")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<ApiResponse<List<ExamResultDTO>>> getChildResults(
            @PathVariable Long childId,
            Authentication auth
    ) {
        List<ExamResultDTO> results = examService.getResultsForParentChild(childId);
        return ResponseEntity.ok(ApiResponse.ok(results));
    }
}
