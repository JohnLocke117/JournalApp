package com.spring.journalapp.service;

import com.spring.journalapp.entity.User;
import com.spring.journalapp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getEntries() {
        return userRepository.findAll();
    }

    public Optional<User> getByID(ObjectId ID) {
        return userRepository.findById(ID);
    }

    public void saveEntry(User journalEntry) {
        userRepository.save(journalEntry);
    }

    public void deleteByID(ObjectId ID) {
        userRepository.deleteById(ID);
    }

    public User findByUsername(String username) { return userRepository.findByUsername(username); }
}
