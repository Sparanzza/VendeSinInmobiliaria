package com.ilerna.vendesininmobiliarias.models;

public class Like {
    private String id;
    private String postId;
    private String userId;
    private long timestamp;

    public Like() {
    }

    public Like(String id, String postId, String userId, long timestamp) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
