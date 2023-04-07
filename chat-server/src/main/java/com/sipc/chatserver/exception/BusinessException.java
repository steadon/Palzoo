package com.sipc.chatserver.exception;

/**
 * 抛出易于理解的异常
 *
 * @author Sterben
 */
public class BusinessException extends RuntimeException {
    private final String message;

    public BusinessException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
