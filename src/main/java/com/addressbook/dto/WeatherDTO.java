package com.addressbook.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WeatherDTO {
    private String location;
    private String temperature;
    private String condition;
    private String humidity;

    @JsonProperty("wind_speed")
    private String windSpeed;

    // Constructors
    public WeatherDTO() {}

    public WeatherDTO(String location, String temperature, String condition, String humidity, String windSpeed) {
        this.location = location;
        this.temperature = temperature;
        this.condition = condition;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
    }

    // Getters and Setters
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getTemperature() { return temperature; }
    public void setTemperature(String temperature) { this.temperature = temperature; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public String getHumidity() { return humidity; }
    public void setHumidity(String humidity) { this.humidity = humidity; }

    public String getWindSpeed() { return windSpeed; }
    public void setWindSpeed(String windSpeed) { this.windSpeed = windSpeed; }
}