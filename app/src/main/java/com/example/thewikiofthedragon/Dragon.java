package com.example.thewikiofthedragon;

public class Dragon {
    private String name;
    private int imageResId;
    private String biography;

    public Dragon(String name, int imageResId, String biography) {
        this.name = name;
        this.imageResId = imageResId;
        this.biography = biography;
    }

    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getBiography() {
        return biography;
    }
}
