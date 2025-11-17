package com.addressbook.dto;

public class ContactDTO {
    private Long id;
    private String picture;
    private String name;
    private String address;
    private String ownerUsername;
    private WeatherDTO weather; // Add weather information

    // Constructors
    public ContactDTO() {}

    public ContactDTO(Long id, String picture, String name, String address, String ownerUsername) {
        this.id = id;
        this.picture = picture;
        this.name = name;
        this.address = address;
        this.ownerUsername = ownerUsername;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPicture() { return picture; }
    public void setPicture(String picture) { this.picture = picture; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getOwnerUsername() { return ownerUsername; }
    public void setOwnerUsername(String ownerUsername) { this.ownerUsername = ownerUsername; }

    public WeatherDTO getWeather() { return weather; }
    public void setWeather(WeatherDTO weather) { this.weather = weather; }
}