package com.tuition.institute.academic;

import com.tuition.institute.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/classes")
@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
public class AdminClassController {

    private final ClassService classService;

    public AdminClassController(ClassService classService) {
        this.classService = classService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClassDTO>>> getAllClasses() {
        return ResponseEntity.ok(ApiResponse.ok(classService.getAllClasses()));
    }
}
