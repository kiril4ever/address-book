package com.addressbook.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class SignupEvent {
    private String username;
    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime signupTime;

    private String eventType = "USER_SIGNUP";

    // Constructors
    public SignupEvent() {}

    public SignupEvent(String username, String email, LocalDateTime signupTime) {
        this.username = username;
        this.email = email;
        this.signupTime = signupTime;
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDateTime getSignupTime() { return signupTime; }
    public void setSignupTime(LocalDateTime signupTime) { this.signupTime = signupTime; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    @Override
    public String toString() {
        return "SignupEvent{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", signupTime=" + signupTime +
                ", eventType='" + eventType + '\'' +
                '}';
    }
}