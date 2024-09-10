package com.example.thewikiofthedragon;

public class Character {

    private String name;
    private int imageResId;
    private String biography;  // Añadimos la biografía

    public Character(String name, int imageResId, String biography) {
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
