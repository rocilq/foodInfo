package org.insbaixcamp.reus.foodinfo;

import android.graphics.Bitmap;

public class Product {
    private String name;
    private Bitmap image;

    public Product(String name, Bitmap image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public Bitmap getImage() {
        return image;
    }
}