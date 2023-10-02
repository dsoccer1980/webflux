package com.poluhin.ss.demo.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class PrometheusCounter {

    private final Counter succesLogin;
    private final Counter unSuccesLogin;

    public PrometheusCounter(MeterRegistry meterRegistry) {
        succesLogin = meterRegistry.counter("success.login");
        unSuccesLogin = meterRegistry.counter("unsuccess.login");
    }

}
