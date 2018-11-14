package com.justeat.vasylt.testtask.screen.main;

import com.justeat.vasylt.testtask.di.BasePresenter;
import com.justeat.vasylt.testtask.model.Restaurant;
import com.justeat.vasylt.testtask.model.RestaurantsHolder;
import com.justeat.vasylt.testtask.network.NetworkClientController;

import java.util.List;

import retrofit2.Response;

/**
 * Created by Vasyl Tkachov on 26.06.2018.
 */
public class MainActivityPresenter implements BasePresenter<MainView> {

    private MainView                view;
    private NetworkClientController networkClientController;
    private String                  postalCode;
    private List<Restaurant>        restaurantsList;

    public MainActivityPresenter() {
        networkClientController = new NetworkClientController();
        networkClientController.init();
    }

    @Override
    public void onAttach(MainView view) {
        this.view = view;
        if (postalCode != null && !postalCode.isEmpty()) {
            view.onLocationPicked(postalCode);
        }
        if (restaurantsList != null && !restaurantsList.isEmpty()) {
            view.onRestaurantsLoaded(restaurantsList);
        }
    }

    @Override
    public void onDetach() {
        view = null;
    }

    public void getRestaurantsList(String postalCode) {
        networkClientController.getRestaurantsList(postalCode, new NetworkClientController.Callback<RestaurantsHolder>() {
            @Override
            public void onSuccess(Response<RestaurantsHolder> response) {
                RestaurantsHolder restaurantHolder = response.body();
                if (restaurantHolder != null) {
                    MainActivityPresenter.this.postalCode = postalCode;
                    if (view != null) {
                        view.onLocationPicked(postalCode);
                        restaurantsList = restaurantHolder.getRestaurantModels();
                        view.onRestaurantsLoaded(restaurantsList);
                    }
                }
            }

            @Override
            public void onFailure(String message) {
                if (view != null) {
                    view.onLoadFail(message);
                }
            }
        });
    }

    public void getRestaurantsList(double latitude, double longitude) {
        String latlng = latitude + "," + longitude;
        networkClientController.getRestaurantsListByLocation(latlng, new NetworkClientController.Callback<RestaurantsHolder>() {
            @Override
            public void onSuccess(Response<RestaurantsHolder> response) {

            }

            @Override
            public void onFailure(String message) {
                if (view != null) {
                    view.onLoadFail(message);
                }
            }
        });
    }
}