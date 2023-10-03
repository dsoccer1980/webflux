package com.poluhin.ss.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrackService {

    private final KafkaService kafkaService;
    private final PrometheusCounter prometheusCounter;

    public void track(String msg) {
        kafkaService.sendMessageToAuthEvent(msg);
        prometheusCounter.getSuccesLogin().increment();
    }

}
