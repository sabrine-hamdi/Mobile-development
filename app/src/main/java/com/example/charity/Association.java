package com.example.charity;

import android.graphics.drawable.Drawable;

public class Association {
    private String title,description;
    private String imgUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return imgUrl;
    }

    public void setImg(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
