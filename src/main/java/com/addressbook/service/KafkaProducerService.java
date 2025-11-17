package com.addressbook.service;

import com.addressbook.dto.SignupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

    private static final String TOPIC = "user-signups";

    @Autowired
    private KafkaTemplate<String, SignupEvent> kafkaTemplate;

    public void sendSignupEvent(SignupEvent event) {
        logger.info("Sending signup event to Kafka: {}", event);

        CompletableFuture<SendResult<String, SignupEvent>> future =
                kafkaTemplate.send(TOPIC, event.getUsername(), event);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                logger.info("Signup event sent successfully for user: {}, offset: {}",
                        event.getUsername(), result.getRecordMetadata().offset());
            } else {
                logger.error("Failed to send signup event for user: {}", event.getUsername(), ex);
            }
        });
    }
}