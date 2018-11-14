package com.justeat.vasylt.testtask.network;

import com.justeat.vasylt.testtask.model.RestaurantsHolder;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Vasyl Tkachov on 26.06.2018.
 */
public interface JustEatClient {

    @GET("restaurants?")
    Call<RestaurantsHolder> getRestaurants(@Query("q") String postalCode);
}
