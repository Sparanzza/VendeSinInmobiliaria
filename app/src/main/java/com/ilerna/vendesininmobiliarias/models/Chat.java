package com.ilerna.vendesininmobiliarias.models;

import java.util.ArrayList;

public class Chat {

    private String id;
    private String userHome;
    private String userAway;
    private boolean isTyping;
    private long timestamp;
    private ArrayList<String> ids;
    private int notificationId;

    public Chat() {
    }


    public Chat(String id, String userHome, String userAway, boolean isTyping, long timestamp, ArrayList<String> ids, int notificationId) {
        this.id = id;
        this.userHome = userHome;
        this.userAway = userAway;
        this.isTyping = isTyping;
        this.timestamp = timestamp;
        this.ids = ids;
        this.notificationId = notificationId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public ArrayList<String> getIds() {
        return ids;
    }

    public void setIds(ArrayList<String> ids) {
        this.ids = ids;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }
}
