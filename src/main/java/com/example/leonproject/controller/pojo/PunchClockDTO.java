package com.example.leonproject.controller.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PunchClockDTO {

    @JsonProperty("username")
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "PunchClockDTO{" +
                "username='" + username + '\'' +
                '}';
    }
}
