package com.tuition.institute.fee;

import com.tuition.institute.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/fees")
@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
public class AdminFeeController {

    private final FeeService feeService;

    public AdminFeeController(FeeService feeService) {
        this.feeService = feeService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FeeInvoiceDTO>>> getAllInvoices() {
        return ResponseEntity.ok(ApiResponse.ok(feeService.getAllInvoices()));
    }
}
