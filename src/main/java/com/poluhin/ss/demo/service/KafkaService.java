package com.poluhin.ss.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    @Value("${spring.kafka.topic.authEvent}")
    private String authEventTopic;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessageToAuthEvent(String msg) {
        kafkaTemplate.send(authEventTopic, msg);
    }

}
