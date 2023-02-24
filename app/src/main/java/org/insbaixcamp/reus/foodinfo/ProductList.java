package org.insbaixcamp.reus.foodinfo;

import java.util.ArrayList;

public class ProductList {
    private static ProductList instance;
    private ArrayList<Product> productList;

    private ProductList() {
        productList = new ArrayList<>();
    }

    public static synchronized ProductList getInstance() {
        if (instance == null) {
            instance = new ProductList();
        }
        return instance;
    }

    public void addProduct(Product product) {
        productList.add(product);
    }

    public ArrayList<Product> getProducts() {
        return productList;
    }
}
