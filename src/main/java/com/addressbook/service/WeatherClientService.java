package com.addressbook.service;

import com.addressbook.dto.WeatherDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

@Service
public class WeatherClientService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherClientService.class);

    @Value("${weather.service.url:http://localhost:8081}")
    private String weatherServiceUrl;

    private final RestTemplate restTemplate;

    public WeatherClientService() {
        this.restTemplate = new RestTemplate();
    }

    public WeatherDTO getWeatherForLocation(String location) {
        try {
            // Clean and encode the location
            String cleanLocation = cleanLocation(location);
            String url = weatherServiceUrl + "/api/weather/" + cleanLocation;
            logger.info("Calling weather service: {}", url);

            ResponseEntity<WeatherDTO> response = restTemplate.getForEntity(url, WeatherDTO.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                logger.info("Weather data retrieved for location: {}", location);
                return response.getBody();
            } else {
                logger.warn("Weather service returned non-success status: {}", response.getStatusCode());
                return createFallbackWeather(location);
            }

        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Weather service returned 404 for location: {}", location);
            return createFallbackWeather(location);
        } catch (HttpClientErrorException e) {
            logger.error("HTTP error calling weather service for location {}: {}", location, e.getMessage());
            return createFallbackWeather(location);
        } catch (ResourceAccessException e) {
            logger.error("Cannot connect to weather service at {} for location {}: {}", weatherServiceUrl, location, e.getMessage());
            return createFallbackWeather(location);
        } catch (Exception e) {
            logger.error("Unexpected error calling weather service for location {}: {}", location, e.getMessage());
            return createFallbackWeather(location);
        }
    }

    public boolean isWeatherServiceAvailable() {
        try {
            String url = weatherServiceUrl + "/api/weather/health";
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            logger.warn("Weather service health check failed for {}: {}", weatherServiceUrl, e.getMessage());
            return false;
        }
    }

    private String cleanLocation(String location) {
        // Extract city name or simplify the address for weather API
        if (location.contains(",")) {
            // Take the part before the first comma (usually the street/city)
            return location.split(",")[0].trim();
        }
        return location.trim();
    }

    private WeatherDTO createFallbackWeather(String location) {
        return new WeatherDTO(
                location,
                "N/A",
                "Service unavailable",
                "N/A",
                "N/A"
        );
    }
}