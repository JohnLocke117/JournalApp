package com.spring.journalapp.controller;

import com.spring.journalapp.entity.JournalEntry;
import com.spring.journalapp.entity.User;
import com.spring.journalapp.service.JournalEntryService;
import com.spring.journalapp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {
    private JournalEntryService journalEntryService;
    private UserService userService;

    public JournalEntryController(JournalEntryService journalEntryService, UserService userService) {
        this.journalEntryService = journalEntryService;
        this.userService = userService;
    }

    @GetMapping("{username}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String username) {
        User user = userService.findByUsername(username);
        List<JournalEntry> list = user.getJournalEntries();
        if (list != null && !list.isEmpty()) return new ResponseEntity<>(list, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("id/{ID}")
    public ResponseEntity<JournalEntry> getJournalByID(@PathVariable ObjectId ID) {
        Optional<JournalEntry> entry =  journalEntryService.getByID(ID);

        if (entry.isPresent()) return new ResponseEntity<>(entry.get(), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("{username}")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry entry, @PathVariable String username) {
        try {
            entry.setDate(LocalDateTime.now());
            journalEntryService.saveEntry(entry, username);
            return new ResponseEntity<>(entry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("{username}/id/{ID}")
    public ResponseEntity<?> updateEntry(@PathVariable String username, @PathVariable ObjectId ID, @RequestBody JournalEntry newEntry) {
        JournalEntry oldEntry = journalEntryService.getByID(ID).orElse(null);

        if (oldEntry != null) {
            oldEntry.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().isEmpty() ? newEntry.getTitle() : oldEntry.getTitle());
            oldEntry.setContent(newEntry.getContent() != null && !newEntry.getContent().isEmpty() ? newEntry.getContent() : oldEntry.getContent());
            journalEntryService.saveEntry(oldEntry);
            return new ResponseEntity<>(oldEntry, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("{username}/id/{ID}")
    public ResponseEntity<?> deleteEntry(@PathVariable String username, @PathVariable ObjectId ID) {
        journalEntryService.deleteByID(username, ID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
