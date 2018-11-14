package com.justeat.vasylt.testtask.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Vasyl Tkachov on 26.06.2018.
 */
public class Restaurant {

    @SerializedName("Name")
    private final String name;

    @SerializedName("RatingAverage")
    private final float rating;

    @SerializedName("Logo")
    private final List<ImageUrl> imageUrls;

    @SerializedName("CuisineTypes")
    private final List<CuisineType> cuisineTypes;

    public Restaurant(String name, float rating, List<ImageUrl> imageUrls, List<CuisineType> cuisineTypes) {
        this.name = name;
        this.rating = rating;
        this.imageUrls = imageUrls;
        this.cuisineTypes = cuisineTypes;
    }

    public String getName() {
        return name;
    }

    public float getRating() {
        return rating;
    }

    public List<ImageUrl> getImageUrls() {
        return imageUrls;
    }

    public List<CuisineType> getCuisineTypes() {
        return cuisineTypes;
    }
}
