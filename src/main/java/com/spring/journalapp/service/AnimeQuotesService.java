package com.spring.journalapp.service;

import com.spring.journalapp.api.response.QuoteResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AnimeQuotesService {
    private static final String API = "https://animechan.io/api/v1/quotes/random";
    private RestTemplate restTemplate;

    public AnimeQuotesService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public QuoteResponse getQuote() {
        String url = API;
        ResponseEntity<QuoteResponse> res = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                QuoteResponse.class
        );
        return res.getBody();
    }
}
