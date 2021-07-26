package com.getitcheap.API.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageResponse {
    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
