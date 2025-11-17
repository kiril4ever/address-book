package com.addressbook.service;

import com.addressbook.dto.SignupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    @KafkaListener(topics = "user-signups", groupId = "addressbook-group")
    public void consumeSignupEvent(SignupEvent event) {
        logger.info("🎉 RECEIVED SIGNUP EVENT - User: {}, Email: {}, Time: {}",
                event.getUsername(), event.getEmail(), event.getSignupTime());

        // Here you can add various business logic for new user signups:

        // 1. Send welcome email
        sendWelcomeEmail(event);

        // 2. Create user profile
        createUserProfile(event);

        // 3. Notify administrators
        notifyAdministrators(event);

        // 4. Initialize user settings
        initializeUserSettings(event);

        logger.info("✅ Completed processing signup event for user: {}", event.getUsername());
    }

    private void sendWelcomeEmail(SignupEvent event) {
        // Simulate sending welcome email
        logger.info("📧 Sending welcome email to: {}", event.getEmail());
        // In real application, integrate with email service like SendGrid, AWS SES, etc.
    }

    private void createUserProfile(SignupEvent event) {
        // Simulate creating user profile
        logger.info("👤 Creating user profile for: {}", event.getUsername());
        // Additional profile setup logic here
    }

    private void notifyAdministrators(SignupEvent event) {
        // Simulate notifying admins
        logger.info("🔔 Notifying administrators about new user: {}", event.getUsername());
        // Could send to admin dashboard, Slack, etc.
    }

    private void initializeUserSettings(SignupEvent event) {
        // Simulate initializing user settings
        logger.info("⚙️ Initializing default settings for user: {}", event.getUsername());
        // Set up default preferences, themes, etc.
    }
}