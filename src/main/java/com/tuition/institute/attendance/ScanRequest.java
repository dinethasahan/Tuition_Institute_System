package com.tuition.institute.attendance;

import jakarta.validation.constraints.NotBlank;

public class ScanRequest {

    @NotBlank(message = "QR Token is required")
    private String qrToken;

    public ScanRequest() {}

    public ScanRequest(String qrToken) {
        this.qrToken = qrToken;
    }

    public String getQrToken() { return qrToken; }
    public void setQrToken(String qrToken) { this.qrToken = qrToken; }
}
