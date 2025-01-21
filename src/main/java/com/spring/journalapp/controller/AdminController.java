package com.spring.journalapp.controller;

import com.spring.journalapp.cache.AppCache;
import com.spring.journalapp.entity.User;
import com.spring.journalapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private UserService userService;
    private AppCache appCache;

    // Constructor based DI:
    public AdminController(UserService userService, AppCache appCache) {
        this.userService = userService;
        this.appCache = appCache;
    }

    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers() {
        List<User> list = userService.getAll();
        if (list != null && !list.isEmpty()) {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create-admin-user")
    public ResponseEntity<?> createAdmin(@RequestBody User user) {
        userService.saveAdmin(user);
        return new ResponseEntity<>("New Admin has been created", HttpStatus.CREATED);
    }

    @GetMapping("clear-app-cache")
    public void clearAppCache() {
        appCache.init();
    }
}
