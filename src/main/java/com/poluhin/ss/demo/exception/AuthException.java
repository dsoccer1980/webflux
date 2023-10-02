package com.poluhin.ss.demo.exception;

import lombok.Getter;

@Getter
public class AuthException extends IAuthException {

    public AuthException(String message, String username) {
        super(message, username);
    }
}
