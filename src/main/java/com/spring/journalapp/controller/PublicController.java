package com.spring.journalapp.controller;

import com.spring.journalapp.entity.User;
import com.spring.journalapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {
    private UserService userService;

    // Constructor Based DI:
    public PublicController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody User user) throws Exception {
        userService.saveEntry(user);
        return new ResponseEntity<>("User CREATED Successfully.", HttpStatus.CREATED);
    }
}
