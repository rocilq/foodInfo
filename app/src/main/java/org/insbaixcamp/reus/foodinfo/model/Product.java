package org.insbaixcamp.reus.foodinfo.model;

import java.util.ArrayList;

public class Product {
    private int _id;
    private ArrayList allergens_from_ingredients = new ArrayList();
    private ArrayList countries = new ArrayList();

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public ArrayList getAllergens_from_ingredients() {
        return allergens_from_ingredients;
    }

    public void setAllergens_from_ingredients(ArrayList allergens_from_ingredients) {
        this.allergens_from_ingredients = allergens_from_ingredients;
    }

    public ArrayList getCountries() {
        return countries;
    }

    public void setCountries(ArrayList countries) {
        this.countries = countries;
    }


}
