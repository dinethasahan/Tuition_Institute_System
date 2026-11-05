package com.tuition.institute.fee;

import com.tuition.institute.common.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/parent/fees")
@PreAuthorize("hasRole('PARENT')")
public class ParentFeeController {

    private final FeeService feeService;

    public ParentFeeController(FeeService feeService) {
        this.feeService = feeService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FeeInvoiceDTO>>> getParentInvoices(Authentication auth) {
        List<FeeInvoiceDTO> invoices = feeService.getInvoicesForParent(auth.getName());
        return ResponseEntity.ok(ApiResponse.ok(invoices));
    }

    @PostMapping("/{invoiceId}/pay")
    public ResponseEntity<ApiResponse<FeeInvoiceDTO>> payInvoice(
            @PathVariable Long invoiceId,
            @Valid @RequestBody PaymentRequest request,
            Authentication auth
    ) {
        FeeInvoiceDTO paid = feeService.payInvoice(invoiceId, request, auth.getName());
        return ResponseEntity.ok(ApiResponse.ok("Payment confirmed and receipt generated", paid));
    }

    @GetMapping("/{invoiceId}/receipt")
    public ResponseEntity<byte[]> downloadReceipt(@PathVariable Long invoiceId) {
        byte[] pdf = feeService.getReceiptPdf(invoiceId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=InvoiceReceipt_" + invoiceId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
