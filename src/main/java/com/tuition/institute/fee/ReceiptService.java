package com.tuition.institute.fee;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class ReceiptService {

    public byte[] generateReceiptPdf(FeeInvoice invoice, Payment payment) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A5, 30, 30, 30, 30);
            PdfWriter.getInstance(document, out);
            document.open();

            // Institutional Header
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, Color.DARK_GRAY);
            Paragraph title = new Paragraph("APEX TUITION INSTITUTE", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Font subFont = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.GRAY);
            Paragraph subtitle = new Paragraph("Official Tuition Fee Receipt • Sri Lanka (LKR)", subFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            subtitle.setSpacingAfter(15);
            document.add(subtitle);

            // Receipt Box
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingAfter(15);

            Font bold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
            Font normal = FontFactory.getFont(FontFactory.HELVETICA, 9);

            table.addCell(createCell("Receipt / Invoice No:", bold));
            table.addCell(createCell(invoice.getInvoiceNumber(), normal));

            table.addCell(createCell("Student Name:", bold));
            table.addCell(createCell(invoice.getStudent().getUser().getFullName(), normal));

            table.addCell(createCell("Student ID:", bold));
            table.addCell(createCell(invoice.getStudent().getStudentCode(), normal));

            table.addCell(createCell("Fee Description:", bold));
            table.addCell(createCell(invoice.getTitle() != null ? invoice.getTitle() : "Monthly Tuition Fee", normal));

            table.addCell(createCell("Amount Paid:", bold));
            table.addCell(createCell("Rs. " + payment.getAmount().toString() + " LKR", bold));

            table.addCell(createCell("Payment Method:", bold));
            table.addCell(createCell(payment.getMethod(), normal));

            table.addCell(createCell("Reference / Trans ID:", bold));
            table.addCell(createCell(payment.getReferenceNo() != null ? payment.getReferenceNo() : "N/A", normal));

            table.addCell(createCell("Payment Date:", bold));
            table.addCell(createCell(payment.getPaidOn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), normal));

            document.add(table);

            // Paid Stamp Note
            Paragraph footer = new Paragraph("PAYMENT STATUS: CONFIRMED & RECORDED\nThank you for learning with Apex Institute.", subFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(15);
            document.add(footer);

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate payment receipt PDF", e);
        }
    }

    private PdfPCell createCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5);
        return cell;
    }
}
