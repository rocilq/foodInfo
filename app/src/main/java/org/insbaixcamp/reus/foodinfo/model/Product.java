package org.insbaixcamp.reus.foodinfo.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class Product {
    @SerializedName("_id")
    private int id;
    @SerializedName("allergens")
    private String allergens;
    @SerializedName("countries")
    private String[] countries;

    public int getId() {
        return id;
    }

    public String getAllergensFromIngredients() {
        return allergens;
    }

    public void setAllergensFromIngredients(String allergensFromIngredients) {
        this.allergens = allergensFromIngredients;
    }

    public String[] getCountries() {
        return countries;
    }

    public void setCountries(String[] countries) {
        this.countries = countries;
    }

    @NonNull
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", allergensFromIngredients='" + allergens + '\'' +
                ", countries=" + Arrays.toString(countries) +
                '}';
    }
}