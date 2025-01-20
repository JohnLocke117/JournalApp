package com.spring.journalapp.controller;

import com.spring.journalapp.api.response.QuoteResponse;
import com.spring.journalapp.api.response.WeatherResponse;
import com.spring.journalapp.entity.User;
import com.spring.journalapp.repository.UserRepository;
import com.spring.journalapp.service.AnimeQuotesService;
import com.spring.journalapp.service.UserService;
import com.spring.journalapp.service.WeatherService;
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
    private WeatherService weatherService;
    private AnimeQuotesService animeQuotesService;

    // Constructor Based DI:
    public UserController(UserService userService, UserRepository userRepository, WeatherService weatherService, AnimeQuotesService animeQuotesService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.weatherService = weatherService;
        this.animeQuotesService = animeQuotesService;
    }

    // Returns Weather:
    @GetMapping("weather")
    public ResponseEntity<?> weather() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        WeatherResponse weatherResponse = weatherService.getWeather("Paris");
        String weather = "";
        if (weatherResponse != null) weather = " Weather feels like " + weatherResponse.getCurrent().getTempC();

        return new ResponseEntity<>("Hi " + username + weather, HttpStatus.OK);
    }

    // Returns a random Anime Quote
    @GetMapping("anime-quote")
    public ResponseEntity<?> animeQuote() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        QuoteResponse quoteResponse = animeQuotesService.getQuote();
        String quote = " Random ahh anime quote " + quoteResponse.getData().getContent();

        return new ResponseEntity<>("Hi " + username + quote, HttpStatus.OK);
    }

    // Updates an existing user:
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

    // Deletes an existing user:
    @DeleteMapping
    public ResponseEntity<?> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        userRepository.deleteByUsername(username);
        return new ResponseEntity<>("User DELETED Successfully.", HttpStatus.NO_CONTENT);
    }
}
