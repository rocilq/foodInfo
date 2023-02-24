package org.insbaixcamp.reus.foodinfo;

import android.graphics.Bitmap;

public class Product {
    private String barcode;
    private String name;
    private Bitmap image;

    public Product(String name, Bitmap image, String barcode) {
        this.name = name;
        this.image = image;
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getCodigo() {
        return barcode;
    }
}