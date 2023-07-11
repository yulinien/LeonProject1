package com.example.leonproject.exception;

public class PunchClockFailException extends RuntimeException {

    private final String errorMessage;

    public PunchClockFailException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
