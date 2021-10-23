package com.alhaj.filegenerator.utils;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import com.alhaj.filegenerator.entity.User;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class UserPDFExporter {



    private String pdfDir="/PdfReportRepo/";

    private String reportFileName="Employee-Report";

    private String reportFileNameDateFormat="dd_MMMM_yyyy";


    private int noOfColumns=4;

    private List<String> columnNames=List.of("Emp Id","Emp","Emp Dept","Emp");

    private final List<User> listUsers;

    public UserPDFExporter(List<User> listUsers) {
        this.listUsers = listUsers;
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("User ID", "E-mail", "Full Name","Roles","Enabled")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();

                    Font font = FontFactory.getFont(FontFactory.HELVETICA);
                    font.setColor(BaseColor.WHITE);
                    header.setPaddingTop(50);

                    header.setBackgroundColor(BaseColor.BLUE);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle,font));
                    table.addCell(header);
                });
    }

    private void writeFirstTableHeader(PdfPTable table) {

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.BLUE);

        cell.setPaddingTop(40);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(BaseColor.WHITE);

        cell.setPhrase(new Phrase("User ID", font));

        table.addCell(cell);

        cell.setPhrase(new Phrase("E-mail", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Full Name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Roles", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Enabled", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table) {
        for (User user : listUsers) {
            table.addCell(String.valueOf(user.getId()));
            table.addCell(user.getEmail());
            table.addCell(user.getFullName());
            table.addCell(user.getUserRole());
            table.addCell(String.valueOf(user.isEnabled()));
        }
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());

    }


    private static Font COURIER = new Font(Font.FontFamily.COURIER, 20, Font.BOLD);
    private static Font COURIER_SMALL = new Font(Font.FontFamily.COURIER, 16, Font.BOLD);
    private static Font COURIER_SMALL_FOOTER = new Font(Font.FontFamily.COURIER, 12, Font.BOLD);


    public void generatePdfReport(HttpServletResponse response)throws DocumentException, IOException {

        Document document = new Document();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
           // addLogo(document);
           // addHeader(writer);
           // addDocTitle(document);
            // createTable(document,noOfColumns);
            // addFooter(document);
            document.close();
            System.out.println("------------------Your PDF Report is ready!-------------------------");

        } catch (FileNotFoundException | DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void addLogo(Document document) {
        try {
           // Resource resource = new ClassPathResource("image/govt.png");

            Image img = Image.getInstance(getClass().getClassLoader().getResource("images/govt.png"));
            img.scaleToFit(45, 45);

            // img.scalePercent(logoImgScale[0], logoImgScale[1]);
            img.setAlignment(Element.ALIGN_RIGHT);
            document.add(img);
        } catch (DocumentException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void addDocTitle(Document document) throws DocumentException {
       // String localDateString = LocalDateTime.now().format(DateTimeFormatter.ofPattern(localDateFormat));
        Paragraph p1 = new Paragraph();
       // leaveEmptyLine(p1, 1);
        p1.add(new Paragraph(reportFileName, COURIER));
        p1.setAlignment(Element.ALIGN_CENTER);
      //  leaveEmptyLine(p1, 1);
        p1.add(new Paragraph("Report generated on " , COURIER_SMALL));

        document.add(p1);

    }

    private void createTable(Document document, int noOfColumns) throws DocumentException {
        Paragraph paragraph = new Paragraph();
        leaveEmptyLine(paragraph, 4);
        document.add(paragraph);

        PdfPTable table = new PdfPTable(noOfColumns);

        table.setWidths(new float[] {1.5f, 3.5f, 3.0f, 3.0f});

        for(int i=0; i<noOfColumns; i++) {
            PdfPCell cell = new PdfPCell(new Phrase(columnNames.get(i)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            //cell.setBorder(2);
            cell.setPadding(5);
            table.addCell(cell);
        }

        table.setHeaderRows(1);
        getDbData(table);
        document.add(table);
    }

    private void getDbData(PdfPTable table) {

        for (User employee : listUsers) {

            table.setWidthPercentage(100);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            try {
                table.setWidths(new float[] {1.5f, 3.5f, 3.0f, 3.0f});
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            table.addCell(employee.getId().toString());
            table.addCell(employee.getEmail());
            table.addCell(employee.getFullName());
            table.addCell(employee.getUserRole());

            System.out.println(employee.getFullName());
        }

    }

    private void addFooter(Document document) throws DocumentException {
        Paragraph p2 = new Paragraph();
        leaveEmptyLine(p2, 3);
        p2.setAlignment(Element.ALIGN_MIDDLE);
        p2.add(new Paragraph(
                "------------------------End Of " +reportFileName+"------------------------",
                COURIER_SMALL_FOOTER));

        document.add(p2);
    }

    private static void leaveEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    private String getPdfNameWithDate() {
        String localDateString = LocalDateTime.now().format(DateTimeFormatter.ofPattern(reportFileNameDateFormat));
        return pdfDir+reportFileName+"-"+localDateString+".pdf";
    }


    private void addHeader(PdfWriter writer){

        Paragraph p2 = new Paragraph();
        leaveEmptyLine(p2, 1);


            PdfPTable header = new PdfPTable(2);
            try {
                // set defaults
                header.setWidths(new int[]{4, 30});
                header.setTotalWidth(527);
                header.setLockedWidth(true);
                header.getDefaultCell().setFixedHeight(50);
                header.getDefaultCell().setBorder(Rectangle.BOTTOM);
                header.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);

                // add image
                // Image logo = Image.getInstance(HeaderFooterPageEvent.class.getResource("/memorynotfound-logo.jpg"));
//            Image logo = Image.getInstance("http://placehold.it/120x120&text=image1");
//            header.addCell(logo);

                Image img = Image.getInstance(getClass().getClassLoader().getResource("images/govt.png"));
                img.scaleToFit(75, 75);
                header.addCell(img);


                // add text
                PdfPCell text = new PdfPCell();
                text.setPaddingBottom(15);
                text.setPaddingLeft(10);
                text.setBorder(Rectangle.BOTTOM);
                text.setBorderColor(BaseColor.LIGHT_GRAY);
                text.addElement(new Phrase("Foreign Scholarship Management", new Font(Font.FontFamily.HELVETICA, 12)));
                text.addElement(new Phrase("Selected Candidate", new Font(Font.FontFamily.HELVETICA, 8)));
                text.addElement(new Phrase("https://mofa.gov.bd/", new Font(Font.FontFamily.HELVETICA, 8)));
                header.addCell(text);

                // write content
                header.writeSelectedRows(0, -1, 34, 803, writer.getDirectContent());




            } catch(DocumentException de) {
                throw new ExceptionConverter(de);
            } catch (MalformedURLException e) {
                throw new ExceptionConverter(e);
            } catch (IOException e) {
                throw new ExceptionConverter(e);
            }




    }

}