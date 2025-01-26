package com.spring.journalapp.service;

import org.springframework.stereotype.Service;

@Service
public class SentimentAnalysisService {
    public String getSentiment(String text) {
        return "POSITIVE";
    }
}
