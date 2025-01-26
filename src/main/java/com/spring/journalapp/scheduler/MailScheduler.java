package com.spring.journalapp.scheduler;

import com.spring.journalapp.entity.JournalEntry;
import com.spring.journalapp.entity.User;
import com.spring.journalapp.enums.Sentiment;
import com.spring.journalapp.repository.UserRepositoryImpl;
import com.spring.journalapp.service.EmailService;
import com.spring.journalapp.service.SentimentAnalysisService;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MailScheduler {
    private EmailService emailService;
    private UserRepositoryImpl userRepository;
    private SentimentAnalysisService sentimentAnalysisService;

    public MailScheduler(EmailService emailService, UserRepositoryImpl userRepository, SentimentAnalysisService sentimentAnalysisService) {
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.sentimentAnalysisService = sentimentAnalysisService;
    }

    @Scheduled(cron = "0 0 8 ? * SUN *")
    public void fetchUsersAndSendSAMail() {
        List<User> userList = userRepository.getUserForSA();

        for (User user: userList) {
            List<JournalEntry> journalEntries = user.getJournalEntries();
            // Filter out the JournalEntries:
            List<Sentiment> sentiments = journalEntries.stream()
                    .filter(x -> x.getDate().isAfter(LocalDateTime.now().minusDays(7)))
                    .map(x -> x.getSentiment()).toList();


            Map<Sentiment, Integer> sentimentCount = new HashMap<>();

            for (Sentiment sentiment: sentiments) {
                if (sentiment != null) sentimentCount.put(sentiment, sentimentCount.getOrDefault(sentiment, 0) + 1);
            }

            Sentiment mostFrequentSentiment = null;
            int maxCount = 0;

            for (Map.Entry<Sentiment, Integer> entry: sentimentCount.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }
            if (mostFrequentSentiment != null) {
                emailService.sendEmail(user.getEmail(), "Sentiment for last 7 days", mostFrequentSentiment.toString());
            }
        }
    }
}
