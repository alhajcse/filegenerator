package com.alhaj.filegenerator.service;

import com.alhaj.filegenerator.entity.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
}
