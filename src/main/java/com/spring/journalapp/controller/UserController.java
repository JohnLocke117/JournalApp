package com.spring.journalapp.controller;

import com.spring.journalapp.entity.User;
import com.spring.journalapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<User> list = userService.getEntries();
        if (list != null && !list.isEmpty()) return new ResponseEntity<>(list, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            userService.saveEntry(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("{username}")
    public ResponseEntity<?> updateUser(@RequestBody User user, @PathVariable String username) {
        User userInDB = userService.findByUsername(username);

        if (userInDB != null) {
            userInDB.setUsername(user.getUsername());
            userInDB.setPassword(user.getPassword());
            userService.saveEntry(userInDB);
            return new ResponseEntity<>(userInDB, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
