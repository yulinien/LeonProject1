package com.example.leonproject.controller.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PunchClockResponseDTO {

    @JsonProperty("username")
    private String username;

    @JsonProperty("response")
    private String response;

    public PunchClockResponseDTO(String username, String response) {
        this.username = username;
        this.response = response;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
