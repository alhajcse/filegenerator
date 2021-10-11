package com.alhaj.filegenerator.service;

import com.alhaj.filegenerator.entity.User;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

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


    public String exportReport(String reportFormat) throws FileNotFoundException, JRException {
        String path = "/Users/user/Downloads/Report";
        List<User> employees = listAll();

        try {
        //load file and compile it
        File file = ResourceUtils.getFile("classpath:user.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employees);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Java Techie");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        if (reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "/employees.html");
        }
        if (reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "/employees.pdf");
        }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return "report generated in path : " + path;
    }


}
