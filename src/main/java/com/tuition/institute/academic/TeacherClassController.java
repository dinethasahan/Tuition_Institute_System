package com.tuition.institute.academic;

import com.tuition.institute.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teacher")
@PreAuthorize("hasRole('TEACHER')")
public class TeacherClassController {

    private final ClassService classService;

    public TeacherClassController(ClassService classService) {
        this.classService = classService;
    }

    @GetMapping("/classes")
    public ResponseEntity<ApiResponse<List<ClassDTO>>> getTeacherClasses(Authentication auth) {
        return ResponseEntity.ok(ApiResponse.ok(classService.getClassesForTeacher(auth.getName())));
    }

    @GetMapping("/sessions/today")
    public ResponseEntity<ApiResponse<List<ClassSessionDTO>>> getTodaySessions(Authentication auth) {
        return ResponseEntity.ok(ApiResponse.ok(classService.getTodaySessionsForTeacher(auth.getName())));
    }

    @GetMapping("/schedule")
    public ResponseEntity<ApiResponse<List<ClassSessionDTO>>> getTeacherSchedule(Authentication auth) {
        return ResponseEntity.ok(ApiResponse.ok(classService.getTodaySessionsForTeacher(auth.getName())));
    }
}
