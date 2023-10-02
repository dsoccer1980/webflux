package com.poluhin.ss.demo.exception;

import com.poluhin.ss.demo.service.KafkaService;
import com.poluhin.ss.demo.service.PrometheusCounter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestControllerAdvice
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ErrorHandler {

    @Autowired
    private KafkaService kafkaService;
    @Autowired
    private PrometheusCounter prometheusCounter;

    @ExceptionHandler({IAuthException.class})
    public void handleMyCustomException(Exception ex) {
        log.error("Authentication failed: {}", ex.getMessage());
        if (ex instanceof PasswordWrongException) {
            kafkaService
                .sendMessageToAuthEvent(String.format("User try to log in %s user account with wrong password", ((IAuthException) ex).getUsername()));
        } else {
            kafkaService.sendMessageToAuthEvent(String.format("Unknow user try to log in %s user account", ((IAuthException) ex).getUsername()));
        }
        prometheusCounter.getUnSuccesLogin().increment();
        throw new ResponseStatusException(HttpStatusCode.valueOf(401), ex.getMessage());
    }

}
