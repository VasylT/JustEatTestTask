package com.justeat.vasylt.testtask.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Vasyl Tkachov on 27.06.2018.
 */
public interface GoogleMapClient {

    @GET("json?&sensor=true")
    Call<String> getGeocodeInfo(@Query("latlng") String latlng);
}
