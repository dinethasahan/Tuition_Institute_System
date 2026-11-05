package com.tuition.institute.user;

import com.tuition.institute.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/staff")
@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
public class StaffController {

    private final UserService userService;

    public StaffController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StaffDTO>>> getStaffList() {
        List<StaffDTO> staff = userService.getAllStaff();
        return ResponseEntity.ok(ApiResponse.ok(staff));
    }
}
