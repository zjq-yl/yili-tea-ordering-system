package com.example.yl_app.models;

public class ChatMessage {
    private String message;
    private boolean isSend;  // true=用户发送, false=AI回复

    public ChatMessage(String message, boolean isSend) {
        this.message = message;
        this.isSend = isSend;
    }

    public String getMessage() { return message; }
    public boolean isSend() { return isSend; }
}