package com.alhaj.filegenerator.resource;

import com.alhaj.filegenerator.entity.User;
import com.alhaj.filegenerator.service.UserServices;
import com.alhaj.filegenerator.utils.UserExcelExporter;
import com.alhaj.filegenerator.utils.UserPDFExporter;
import com.itextpdf.text.DocumentException;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class UserResource {

    @Autowired
    private UserServices service;

    @GetMapping("/users/export/pdf/{id}")
    public void exportToPDF(HttpServletResponse response, @PathVariable String id) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        System.out.println("Program "+id);

        List<User> listUsers = service.listAll();

        UserPDFExporter exporter = new UserPDFExporter(listUsers);
        //exporter.export(response);
        exporter.generatePdfReport(response);

    }


    @GetMapping("/users/export/excel/{id}")
    public void exportToExcel(HttpServletResponse response,@PathVariable String id) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<User> listUsers = service.listAll();

        System.out.println("Program "+id);

        UserExcelExporter excelExporter = new UserExcelExporter(listUsers);

        excelExporter.export(response);
    }




    @GetMapping("/report/{format}")
    public String generateReport(@PathVariable String format) throws FileNotFoundException, JRException {
        return service.exportReport(format);
    }

}
