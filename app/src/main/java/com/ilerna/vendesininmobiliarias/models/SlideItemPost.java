package com.ilerna.vendesininmobiliarias.models;

public class SlideItemPost {
    private String image;
    private String title;

    public SlideItemPost(String url, String title) {
        this.image = url;
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
