package com.tuition.institute.fee;

import jakarta.validation.constraints.NotBlank;

public class PaymentRequest {

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    private String transactionRef;

    public PaymentRequest() {}

    public PaymentRequest(String paymentMethod, String transactionRef) {
        this.paymentMethod = paymentMethod;
        this.transactionRef = transactionRef;
    }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getTransactionRef() { return transactionRef; }
    public void setTransactionRef(String transactionRef) { this.transactionRef = transactionRef; }
}
