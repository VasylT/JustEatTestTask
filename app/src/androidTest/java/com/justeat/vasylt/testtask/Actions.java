package com.justeat.vasylt.testtask;

import com.justeat.vasylt.testtask.model.Restaurant;
import com.justeat.vasylt.testtask.model.RestaurantsHolder;
import com.justeat.vasylt.testtask.network.NetworkClientController;

import org.junit.Assert;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import retrofit2.Response;

import static junit.framework.Assert.fail;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

/**
 * Created by Vasyl Tkachov on 27.06.2018.
 */
public class Actions {

    private final int       TIMEOUT          = 10;
    private final boolean[] isRequestSuccess = {false};

    private NetworkClientController controller;
    private CountDownLatch          latch;
    private List<Restaurant>        restaurantsList;


    public Actions() {
        controller = new NetworkClientController();
        controller.init();
    }

    public void getRestaurants(String postalCode, int expectedMinNumber) throws InterruptedException {
        latch = new CountDownLatch(1);
        isRequestSuccess[0] = false;
        restaurantsList = null;

        controller.getRestaurantsList(postalCode, new NetworkClientController.Callback<RestaurantsHolder>() {
            @Override public void onSuccess(Response<RestaurantsHolder> response) {
                isRequestSuccess[0] = true;
                RestaurantsHolder restaurantHolder = response.body();
                if (restaurantHolder != null) {
                    restaurantsList = restaurantHolder.getRestaurantModels();
                }
                latch.countDown();
            }

            @Override public void onFailure(String message) {
                fail(message);
                latch.countDown();
            }
        });

        latch.await(TIMEOUT, TimeUnit.SECONDS);
        Assert.assertTrue(isRequestSuccess[0]);
        Assert.assertNotNull(restaurantsList);
        Assert.assertThat(restaurantsList.size(), greaterThanOrEqualTo(expectedMinNumber));
    }

    public void getRestaurants(double latitude, double longitude, int expectedMinNumber) throws InterruptedException {
        latch = new CountDownLatch(1);
        isRequestSuccess[0] = false;
        restaurantsList = null;

        String latlng = latitude + "," + longitude;
        controller.getRestaurantsListByLocation(latlng, new NetworkClientController.Callback<RestaurantsHolder>() {
            @Override public void onSuccess(Response<RestaurantsHolder> response) {
                isRequestSuccess[0] = true;
                RestaurantsHolder restaurantHolder = response.body();
                if (restaurantHolder != null) {
                    restaurantsList = restaurantHolder.getRestaurantModels();
                }
                latch.countDown();
            }

            @Override public void onFailure(String message) {
                fail(message);
                latch.countDown();
            }
        });

        latch.await(TIMEOUT, TimeUnit.SECONDS);
        Assert.assertTrue(isRequestSuccess[0]);
        Assert.assertNotNull(restaurantsList);
        Assert.assertThat(restaurantsList.size(), greaterThanOrEqualTo(expectedMinNumber));
    }
}
