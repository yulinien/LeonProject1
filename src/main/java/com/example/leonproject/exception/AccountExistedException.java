package com.example.leonproject.exception;

import jakarta.servlet.http.HttpServletRequest;

public class AccountExistedException extends RuntimeException {

    private final HttpServletRequest request;

    public AccountExistedException(HttpServletRequest request) {

        this.request = request;
    }

    public HttpServletRequest getRequest() {

        return request;
    }
}
