package com.ilerna.vendesininmobiliarias.models;

public class Chat {

    private String userHome;
    private String userAway;
    private boolean isTyping;
    private long timestamp;

    public Chat() {
    }

    public Chat(String userHome, String userAway, boolean isTyping, long timestamp) {
        this.userHome = userHome;
        this.userAway = userAway;
        this.isTyping = isTyping;
        this.timestamp = timestamp;
    }

    public String getUserHome() {
        return userHome;
    }

    public void setUserHome(String userHome) {
        this.userHome = userHome;
    }

    public String getUserAway() {
        return userAway;
    }

    public void setUserAway(String userAway) {
        this.userAway = userAway;
    }

    public boolean isTyping() {
        return isTyping;
    }

    public void setTyping(boolean typing) {
        isTyping = typing;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
