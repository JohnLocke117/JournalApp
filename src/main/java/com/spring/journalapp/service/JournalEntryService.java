package com.spring.journalapp.service;

import com.spring.journalapp.entity.JournalEntry;
import com.spring.journalapp.entity.User;
import com.spring.journalapp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {
    private final UserService userService;
    private JournalEntryRepository journalEntryRepository;

    public JournalEntryService(JournalEntryRepository journalEntryRepository, UserService userService) {
        this.journalEntryRepository = journalEntryRepository;
        this.userService = userService;
    }

    public List<JournalEntry> getEntries() {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> getByID(ObjectId ID) {
        return journalEntryRepository.findById(ID);
    }

    public void saveEntry(JournalEntry journalEntry) {
        journalEntryRepository.save(journalEntry);
    }

    @Transactional
    public void saveEntry(JournalEntry journalEntry, String username) throws Exception {
        try {
            User user = userService.findByUsername(username);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry savedEntry = journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(savedEntry);
            userService.saveEntry(user);
        } catch (Exception e) {
            throw new RuntimeException("An error has occurred while saving the Entry.", e);
        }
    }

    public void deleteByID(String username, ObjectId ID) {
        User user = userService.findByUsername(username);
        user.getJournalEntries().removeIf(x -> x.getID().equals(ID));
        userService.saveEntry(user);

        journalEntryRepository.deleteById(ID);
    }
}
