package com.ilerna.vendesininmobiliarias.models;

public class User {
    private String id;
    private String username;
    private String email;
    private String phoneNumber;
    private String password;
    private long timestamp;
    private String photoProfile;
    private long lastConnection;
    private boolean isOnline;


    public User() {
    }

    public User(String id, String username, String email, String phoneNumber, String password, long timestamp, String photoProfile, long lastConnection, boolean isOnline) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.timestamp = timestamp;
        this.photoProfile = photoProfile;
        this.lastConnection = lastConnection;
        this.isOnline = isOnline;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getPhotoProfile() {
        return photoProfile;
    }

    public void setPhotoProfile(String photoProfile) {
        this.photoProfile = photoProfile;
    }

    public long getLastConnection() {
        return lastConnection;
    }

    public void setLastConnection(long lastConnection) {
        this.lastConnection = lastConnection;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}
