package com.example.happyhomes.Model;

public class Message {
    private String messageText;
    private String senderId;
    private String receiverId;
    private long timestamp;

    public Message() {
        // Default constructor required for calls to DataSnapshot.getValue(Message.class)
    }

    public Message(String messageText, String senderId, String receiverId, long timestamp) {
        this.messageText = messageText;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.timestamp = timestamp;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
