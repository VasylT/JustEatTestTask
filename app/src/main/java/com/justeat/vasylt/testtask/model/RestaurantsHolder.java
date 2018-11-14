package com.justeat.vasylt.testtask.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Vasyl Tkachov on 27.06.2018.
 */
public class RestaurantsHolder {

    @SerializedName("Restaurants")
    private final List<Restaurant> restaurants;

    public RestaurantsHolder(List<Restaurant> restaurantModels) {
        this.restaurants = restaurantModels;
    }

    public List<Restaurant> getRestaurantModels() {
        return restaurants;
    }
}
