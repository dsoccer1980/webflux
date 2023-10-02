package com.poluhin.ss.demo.exception;

public class PasswordWrongException extends IAuthException {

    public PasswordWrongException(String message, String username) {
        super(message, username);
    }
}
