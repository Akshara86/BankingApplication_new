package com.example.Banking.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class ResponseMessage {
    private String status;
    private String message;
    private Map<String, Object> data; // Optional: additional data like id, username

    // Constructor for status and message
    public ResponseMessage(String status, String message) {
        this.status = status;
        this.message = message;
    }

    // Constructor for status, message, and data
    public ResponseMessage(String status, String message, Map<String, Object> data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
