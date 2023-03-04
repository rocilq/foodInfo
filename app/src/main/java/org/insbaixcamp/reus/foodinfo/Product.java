package org.insbaixcamp.reus.foodinfo;

public class Product {
    private String name;
    private String imageUrl;
    private String barcode;

    public Product(String name, String imageUrl, String barcode) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getBarcode() {
        return barcode;
    }
}