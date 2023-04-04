package com.sipc.loginserver.exception;

public class BusinessException extends RuntimeException {
    private final String message;

    public BusinessException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
