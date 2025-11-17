package com.addressbook.controller;

import com.addressbook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/hello")
    @ResponseBody
    public String sayHello() {
        return "Hello from Spring Boot! The application is running successfully.";
    }

    @GetMapping("/create-test-user")
    @ResponseBody
    public String createTestUser() {
        try {
            userService.registerUser("testuser", "test@example.com", "password");
            return "Test user created: username='testuser', password='password'";
        } catch (RuntimeException e) {
            return "Test user already exists: " + e.getMessage();
        }
    }
}