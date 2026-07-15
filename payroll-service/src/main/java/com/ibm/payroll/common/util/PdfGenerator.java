package com.ibm.payroll.common.util;

import com.ibm.payroll.entity.Payslip;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.awt.Color;
import java.time.format.DateTimeFormatter;

public class PdfGenerator {

    public static byte[] generatePayslipPdf(Payslip payslip) {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Font styles
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.DARK_GRAY);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
            Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.BLACK);
            Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);

            // Title
            Paragraph title = new Paragraph("PAYSLIP RECEIPT", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Details table
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);
            table.setSpacingAfter(20);

            // Add cells helper
            addTableCell(table, "Employee ID:", payslip.getEmployeeId(), labelFont, valueFont);
            addTableCell(table, "Employee Name:", payslip.getEmployeeName() != null ? payslip.getEmployeeName() : "N/A", labelFont, valueFont);
            addTableCell(table, "Designation:", payslip.getDesignation() != null ? payslip.getDesignation() : "N/A", labelFont, valueFont);
            addTableCell(table, "Period:", payslip.getPeriod(), labelFont, valueFont);
            addTableCell(table, "Generated At:", payslip.getGeneratedAt() != null ? payslip.getGeneratedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "N/A", labelFont, valueFont);

            document.add(table);

            // Financials Table
            PdfPTable finTable = new PdfPTable(2);
            finTable.setWidthPercentage(100);
            
            // Header Row
            PdfPCell cell1 = new PdfPCell(new Phrase("Description", headerFont));
            cell1.setBackgroundColor(Color.GRAY);
            cell1.setPadding(8);
            finTable.addCell(cell1);

            PdfPCell cell2 = new PdfPCell(new Phrase("Amount", headerFont));
            cell2.setBackgroundColor(Color.GRAY);
            cell2.setPadding(8);
            finTable.addCell(cell2);

            // Earnings
            addFinRow(finTable, "Earnings (Basic + HRA + Allowances)", payslip.getEarnings(), valueFont);
            // Deductions
            addFinRow(finTable, "Deductions", payslip.getDeductions(), valueFont);
            // Net Pay
            addFinRow(finTable, "Net Pay", payslip.getNetPay(), labelFont);

            document.add(finTable);

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }

    private static void addTableCell(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
        PdfPCell cellLabel = new PdfPCell(new Phrase(label, labelFont));
        cellLabel.setBorder(Rectangle.NO_BORDER);
        cellLabel.setPadding(5);
        table.addCell(cellLabel);

        PdfPCell cellValue = new PdfPCell(new Phrase(value, valueFont));
        cellValue.setBorder(Rectangle.NO_BORDER);
        cellValue.setPadding(5);
        table.addCell(cellValue);
    }

    private static void addFinRow(PdfPTable table, String description, Double amount, Font font) {
        PdfPCell descCell = new PdfPCell(new Phrase(description, font));
        descCell.setPadding(8);
        table.addCell(descCell);

        String amtStr = amount != null ? String.format("$%.2f", amount) : "$0.00";
        PdfPCell amtCell = new PdfPCell(new Phrase(amtStr, font));
        amtCell.setPadding(8);
        amtCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(amtCell);
    }
}
