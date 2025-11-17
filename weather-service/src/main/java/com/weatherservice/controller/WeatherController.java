package com.weatherservice.controller;

import com.weatherservice.dto.WeatherResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/api/weather")
@CrossOrigin(origins = "*")
public class WeatherController {

    private final Random random = new Random();
    private final String[] weatherConditions = {
            "Sunny", "Cloudy", "Rainy", "Snowy", "Windy", "Foggy", "Stormy"
    };

    @GetMapping("/{location}")
    public WeatherResponse getWeather(@PathVariable String location) {
        // Simulate weather data based on location
        // In a real application, this would call a weather API like OpenWeatherMap

        // Simulate some processing time
        simulateDelay();

        // Generate random weather data based on location
        String temperature = generateTemperature(location);
        String condition = generateCondition();
        String humidity = generateHumidity();
        String windSpeed = generateWindSpeed();

        return new WeatherResponse(
                location,
                temperature + "°C",
                condition,
                humidity + "%",
                windSpeed + " km/h"
        );
    }

    @GetMapping("/health")
    public String health() {
        return "Weather Service is running!";
    }

    private void simulateDelay() {
        try {
            // Simulate API call delay (100-500ms)
            Thread.sleep(100 + random.nextInt(400));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private String generateTemperature(String location) {
        // Generate temperature based on location name (for demo purposes)
        int baseTemp = Math.abs(location.hashCode() % 35); // 0-34 degrees
        return String.valueOf(5 + baseTemp); // 5-39 degrees
    }

    private String generateCondition() {
        return weatherConditions[random.nextInt(weatherConditions.length)];
    }

    private String generateHumidity() {
        return String.valueOf(30 + random.nextInt(60)); // 30-89%
    }

    private String generateWindSpeed() {
        return String.valueOf(5 + random.nextInt(25)); // 5-29 km/h
    }
}