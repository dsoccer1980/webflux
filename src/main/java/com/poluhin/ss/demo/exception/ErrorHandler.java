package com.poluhin.ss.demo.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestControllerAdvice
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ErrorHandler {

    @ExceptionHandler({AuthException.class, UsernameNotFoundException.class})
    public void handleMyCustomException(Exception ex) {
        log.error("Authentication failed: {}", ex.getMessage());
        throw new ResponseStatusException(HttpStatusCode.valueOf(401), ex.getMessage());
    }

}
