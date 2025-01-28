package com.spring.journalapp.controller;

import com.spring.journalapp.entity.User;
import com.spring.journalapp.service.UserDetailsServiceImpl;
import com.spring.journalapp.service.UserService;
import com.spring.journalapp.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {
    private UserService userService;
    private AuthenticationManager authenticationManager;
    private UserDetailsServiceImpl userDetailsService;
    private JWTUtils jwtUtils;

    // Constructor Based DI:
    public PublicController(UserService userService,
                            AuthenticationManager authenticationManager,
                            UserDetailsServiceImpl userDetailsService,
                            JWTUtils jwtUtils) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) throws Exception {
        userService.saveEntry(user);
        return new ResponseEntity<>("User CREATED Successfully.", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

            String jwt = jwtUtils.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception occurred while creating authentication token", e);
            return new ResponseEntity<>("Incorrect UserName or Password", HttpStatus.BAD_REQUEST);
        }

    }
}
