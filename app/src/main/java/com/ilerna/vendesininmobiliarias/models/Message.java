package com.ilerna.vendesininmobiliarias.models;

public class Message {

    private String id;
    private String senderId;
    private String recieverId;
    private String chatId;
    public String text;
    private long timestamp;
    private boolean isChecked;

    public Message() {
    }

    public Message(String id, String senderId, String recieverId, String chatId, String text, long timestamp, boolean isChecked) {
        this.id = id;
        this.senderId = senderId;
        this.recieverId = recieverId;
        this.chatId = chatId;
        this.text = text;
        this.timestamp = timestamp;
        this.isChecked = isChecked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRecieverId() {
        return recieverId;
    }

    public void setRecieverId(String recieverId) {
        this.recieverId = recieverId;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
