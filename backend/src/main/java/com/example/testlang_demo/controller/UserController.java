package com.example.testlang_demo.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        // In a real app, you would verify the password.
        // For this project, we just return a success message with a fake token.
        return ResponseEntity.ok(Map.of("token", "sample_token"));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUser(@PathVariable int id) {
        // In a real app, you would look up the user in a database.
        // For this project, we return some sample data.
        return ResponseEntity.ok(Map.of("id", id, "username", "admin"));
    }
}