package com.tuition.institute.fee;

public class FeeInvoiceDTO {
    private String id;
    private String invoiceNumber;
    private String studentId;
    private String studentName;
    private String title;
    private String month;
    private Double amount;
    private String dueDate;
    private String paidDate;
    private String status; // Paid, Pending, Overdue
    private String paymentMethod;
    private String transactionId;

    public FeeInvoiceDTO() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }

    public String getPaidDate() { return paidDate; }
    public void setPaidDate(String paidDate) { this.paidDate = paidDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
}
