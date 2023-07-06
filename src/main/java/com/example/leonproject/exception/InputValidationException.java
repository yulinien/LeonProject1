package com.example.leonproject.exception;

public class InputValidationException extends RuntimeException {

    private final String errorMessage;

    public InputValidationException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
