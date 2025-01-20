package com.spring.journalapp.service;

import com.spring.journalapp.api.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
    @Value("${weather.api.key}")
    private String apiKey;
    private static final String API = "http://api.weatherapi.com/v1/current.json?key=API_KEY&q=CITY";
    private RestTemplate restTemplate;

    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public WeatherResponse getWeather(String city) {
        String url = API.replace("API_KEY", apiKey).replace("CITY", city);
        ResponseEntity<WeatherResponse> res = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                WeatherResponse.class
        );

        return res.getBody();
    }
}
