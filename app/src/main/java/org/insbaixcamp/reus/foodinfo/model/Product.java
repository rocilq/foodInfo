package org.insbaixcamp.reus.foodinfo.model;

import java.util.ArrayList;

public class Product {
    private int _id;
    private String allergens_from_ingredients;
    private String[] countries;

    public int get_id() {
        return _id;
    }

    public String getAllergens_from_ingredients() {
        return allergens_from_ingredients;
    }

    public String[] getCountries() {
        return countries;
    }


}
