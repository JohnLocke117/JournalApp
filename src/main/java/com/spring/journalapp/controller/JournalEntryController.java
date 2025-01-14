package com.spring.journalapp.controller;

import com.spring.journalapp.entity.JournalEntry;
import com.spring.journalapp.entity.User;
import com.spring.journalapp.service.JournalEntryService;
import com.spring.journalapp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {
    private JournalEntryService journalEntryService;
    private UserService userService;

    // Constructor-Based DI:
    public JournalEntryController(JournalEntryService journalEntryService, UserService userService) {
        this.journalEntryService = journalEntryService;
        this.userService = userService;
    }

    // Get all Journals of a User:
    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesOfUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.findByUsername(username);
        List<JournalEntry> list = user.getJournalEntries();
        if (list != null && !list.isEmpty()) return new ResponseEntity<>(list, HttpStatus.OK);
        return new ResponseEntity<>("No entries exist for the User", HttpStatus.NOT_FOUND);
    }

    // Get a Journal entry of a User by ID:
    // Also checks if the given ID exists in the User's entries:
    @GetMapping("id/{ID}")
    public ResponseEntity<JournalEntry> getJournalByID(@PathVariable ObjectId ID) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.findByUsername(username);
        List<JournalEntry> list = user.getJournalEntries().stream().filter(x -> x.getID().equals(ID)).toList();

        if (!list.isEmpty()) {
            Optional<JournalEntry> entry =  journalEntryService.getByID(ID);
            if (entry.isPresent()) return new ResponseEntity<>(entry.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Create a new Journal Entry:
    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry entry) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            journalEntryService.saveEntry(entry, username);
            return new ResponseEntity<>(entry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Update a Journal entry:
    @PutMapping("id/{ID}")
    public ResponseEntity<?> updateEntry(@PathVariable ObjectId ID, @RequestBody JournalEntry newEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Checks if given username and ID match:
        User user = userService.findByUsername(username);
        List<JournalEntry> list = user.getJournalEntries().stream().filter(x -> x.getID().equals(ID)).toList();

        if (!list.isEmpty()) {
            JournalEntry oldEntry = journalEntryService.getByID(ID).orElse(null);

            if (oldEntry != null) {
                oldEntry.setTitle(!newEntry.getTitle().isEmpty() ? newEntry.getTitle() : oldEntry.getTitle());
                oldEntry.setContent(newEntry.getContent() != null && !newEntry.getContent().isEmpty() ? newEntry.getContent() : oldEntry.getContent());
                journalEntryService.saveEntry(oldEntry);
                return new ResponseEntity<>(oldEntry, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Delete Journal Entry by ID:
    @DeleteMapping("id/{ID}")
    public ResponseEntity<?> deleteEntry(@PathVariable ObjectId ID) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        boolean removed = journalEntryService.deleteByID(username, ID);
        if (removed) return new ResponseEntity<>("Journal Entry removed successfully", HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
