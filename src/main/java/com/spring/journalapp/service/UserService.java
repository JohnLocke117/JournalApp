package com.spring.journalapp.service;

import com.spring.journalapp.entity.User;
import com.spring.journalapp.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserService {
    private UserRepository userRepository;
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Constructor Based DI:
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    // This method is used when a new user is to be created, the password is encrypted:
    public void saveEntry(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of("USER"));
        userRepository.save(user);
    }

    // This method creates a new ADMIN User:
    public void saveAdmin(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of("USER", "ADMIN"));
        userRepository.save(user);
    }

    // This method is used when we want to update the User, here the password is not encrypted again:
    public void updateUserEntry(User user) {
        userRepository.save(user);
    }

    public User findByUsername(String username) { return userRepository.findByUsername(username); }
}
