package com.example.springkeycloak.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/greeting")
public class HomeController {

    @PreAuthorize("hasRole('user')")
    @GetMapping("/user")
    public Map<String, String> greetUser() {
        Map<String, String> response = new HashMap<>();

        response.put("to", "User");
        response.put("message", "Welcome User!");

        return response;
    }

    @PreAuthorize("hasRole('admin')")
    @GetMapping("/admin")
    public Map<String, String> greetAdmin() {
        Map<String, String> response = new HashMap<>();

        response.put("to", "Admin");
        response.put("message", "Welcome Admin!");

        return response;
    }
}
