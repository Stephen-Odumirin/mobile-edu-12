package com.skool.model;

public class LectureCategory {
    private String categoyName;
    private int imageResource;

    public LectureCategory(String categoryName, int imageResource) {
        this.categoyName = categoryName;
        this.imageResource = imageResource;
    }

    public String getCategoryName() {
        return categoyName;
    }

    public int getImageResource() {
        return imageResource;
    }
}
