package com.ilerna.vendesininmobiliarias.models;

public class Comment {

    private String id;
    private String comment;
    private String userId;
    private String postId;
    long timestamp;

    public Comment(){

    }

    public Comment(String id, String comment, String userId, String postId, long timestamp) {
        this.id = id;
        this.comment = comment;
        this.userId = userId;
        this.postId = postId;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
