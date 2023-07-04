package com.example.leonproject.controller.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegistrationResponseDTO {

    @JsonProperty("status")
    private int status;
    @JsonProperty("errorMessage")
    private String errorMessage;

    public RegistrationResponseDTO(int status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
