package com.poluhin.ss.demo.exception;

import lombok.Getter;

@Getter
public class IAuthException extends RuntimeException {

    private final String username;

    public IAuthException(String message, String username) {
        super(message);
        this.username = username;
    }

}
