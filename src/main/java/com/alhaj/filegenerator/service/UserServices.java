package com.alhaj.filegenerator.service;

import com.alhaj.filegenerator.entity.User;
import com.itextpdf.text.Document;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServices {

    public List<User> listAll() {

        List<User> userList=new ArrayList<>();

        userList.clear();

        for(int i=0;i<100;i++){

            User user=new User();
            user.setId(i);
            user.setEmail("alhaj@gmail.com"+i);
            user.setUserRole("Admin"+i);
            user.setFullName("Alhaj Uddin"+i);
            user.setPassword("12345678");
            user.setEnabled(true);
            userList.add(user);
        }

        return userList;
    }


    public String exportReport(String reportFormat, HttpServletResponse response) throws FileNotFoundException, JRException {
        String path = "/Users/user/Downloads/Report";
        List<User> employees = listAll();

        try {
        //load file and compile it
        File file = ResourceUtils.getFile("classpath:reportwithbox.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employees);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "BANBEIS");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        if (reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "/employees.html");
        }
        if (reportFormat.equalsIgnoreCase("pdf")) {
           // Output PDF to path
          //  JasperExportManager.exportReportToPdfFile(jasperPrint, path + "/employees.pdf");

            // Output PDF to HTTP Response
            response.setContentType("application/pdf");
            response.addHeader("Content-disposition", "attachment; filename=report.pdf");
            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());

        }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return "report generated in path : " + path;
    }


}
