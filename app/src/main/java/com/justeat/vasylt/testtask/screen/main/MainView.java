package com.justeat.vasylt.testtask.screen.main;

import com.justeat.vasylt.testtask.model.Restaurant;

import java.util.List;

/**
 * Created by Vasyl Tkachov on 26.06.2018.
 */
public interface MainView {

    void onLocationPicked(String postalCode);

    void onRestaurantsLoaded(List<Restaurant> items);

    void onLoadFail(String error);
}
