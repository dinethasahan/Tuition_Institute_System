package com.tuition.institute.exam;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.tuition.institute.user.Student;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReportCardService {

    public byte[] generateReportCardPdf(Student student, List<ExamResult> results) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(document, out);
            document.open();

            // Header Section
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.DARK_GRAY);
            Paragraph title = new Paragraph("APEX TUITION & SCIENCE INSTITUTE", headerFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Font subHeaderFont = FontFactory.getFont(FontFactory.HELVETICA, 11, Color.GRAY);
            Paragraph subtitle = new Paragraph("Official Student Term Academic Report Card • Sri Lanka", subHeaderFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            subtitle.setSpacingAfter(18);
            document.add(subtitle);

            // Student Information Box
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setSpacingAfter(15);

            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

            infoTable.addCell(createCell("Student Name: " + student.getUser().getFullName(), boldFont));
            infoTable.addCell(createCell("Student Index / ID: " + student.getStudentCode(), boldFont));
            infoTable.addCell(createCell("Grade / Batch: " + student.getGrade(), normalFont));
            infoTable.addCell(createCell("Date Generated: " + LocalDate.now(), normalFont));

            document.add(infoTable);

            // Results Table
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{3.5f, 2.5f, 1.5f, 1.2f, 2.0f});

            Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
            addTableHeader(table, "Exam Title", tableHeaderFont);
            addTableHeader(table, "Subject", tableHeaderFont);
            addTableHeader(table, "Score", tableHeaderFont);
            addTableHeader(table, "Grade", tableHeaderFont);
            addTableHeader(table, "Status", tableHeaderFont);

            for (ExamResult res : results) {
                table.addCell(new Phrase(res.getExam().getTitle(), normalFont));
                table.addCell(new Phrase(res.getExam().getClassEntity().getSubject().getName(), normalFont));
                table.addCell(new Phrase(res.getMarks() != null ? res.getMarks().toString() : "Absent", normalFont));
                table.addCell(new Phrase(res.getGrade() != null ? res.getGrade() : "-", boldFont));
                table.addCell(new Phrase(res.getMarks() != null && res.getMarks().doubleValue() >= 40.0 ? "PASS" : "NEEDS RETAKE", normalFont));
            }

            document.add(table);

            // Remarks Section
            Paragraph footer = new Paragraph("\nInstitutional Remarks: Performance is verified against official continuous assessment records. Issued for parent review.", subHeaderFont);
            footer.setSpacingBefore(20);
            document.add(footer);

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF report card", e);
        }
    }

    private PdfPCell createCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(4);
        return cell;
    }

    private void addTableHeader(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(new Color(49, 46, 129)); // Indigo 900
        cell.setPadding(6);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }
}
