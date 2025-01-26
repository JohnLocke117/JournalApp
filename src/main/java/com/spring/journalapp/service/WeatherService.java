package com.spring.journalapp.service;

import com.spring.journalapp.api.response.WeatherResponse;
import com.spring.journalapp.cache.AppCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
    @Value("${weather.api.key}")
    private String apiKey;
    private RestTemplate restTemplate;
    private AppCache appCache;
    private RedisService redisService;

    public WeatherService(RestTemplate restTemplate, AppCache appCache, RedisService redisService) {
        this.restTemplate = restTemplate;
        this.appCache = appCache;
        this.redisService = redisService;
    }

    public WeatherResponse getWeather(String city) {
        // Check Redis Cache if data exists:
        WeatherResponse weatherResponse = redisService.get(city, WeatherResponse.class);
        if (weatherResponse != null) return weatherResponse;
        else {
            String url = appCache.APP_CACHE.get("weather_api").replace("<API_KEY>", apiKey).replace("<CITY>", city);
            ResponseEntity<WeatherResponse> res = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    WeatherResponse.class
            );

            // Save in Redis Cache as well:
            if (res.getBody() != null) redisService.set(city, res.getBody(), 300L);
            return res.getBody();
        }
    }
}
