package com.spring.journalapp.controller;

import com.spring.journalapp.entity.User;
import com.spring.journalapp.repository.UserRepository;
import com.spring.journalapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    private UserRepository userRepository;

    // Constructor Based DI:
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User userInDB = userService.findByUsername(username);

        userInDB.setUsername(user.getUsername());
        userInDB.setPassword(user.getPassword());
        userService.saveEntry(userInDB);
        return new ResponseEntity<>("User UPDATED Successfully.", HttpStatus.ACCEPTED);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        userRepository.deleteByUsername(username);
        return new ResponseEntity<>("User DELETED Successfully.", HttpStatus.NO_CONTENT);
    }
}
