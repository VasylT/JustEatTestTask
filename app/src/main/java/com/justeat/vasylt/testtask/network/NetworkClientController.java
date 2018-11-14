package com.justeat.vasylt.testtask.network;

import android.support.annotation.NonNull;

import com.justeat.vasylt.testtask.BuildConfig;
import com.justeat.vasylt.testtask.model.RestaurantsHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.justeat.vasylt.testtask.Constants.API_AUTH;
import static com.justeat.vasylt.testtask.Constants.API_URL;
import static com.justeat.vasylt.testtask.Constants.GOOGLE_API_URL;

/**
 * Created by Vasyl Tkachov on 27.06.2018.
 */
public class NetworkClientController {

    private JustEatClient   justEatClient;
    private GoogleMapClient googleMapClient;

    public interface Callback<T> {
        void onSuccess(Response<T> response);

        void onFailure(String message);
    }

    public void init() {
        if (justEatClient == null) {
            Interceptor interceptor = chain -> {
                Request.Builder builder = chain.request().newBuilder()
                        .header("Accept-Tenant", "uk")
                        .header("Accept-Language", "en-GB")
                        .header("Authorization", API_AUTH);

                return chain.proceed(builder.build());
            };

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(BuildConfig.DEBUG ?
                    HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    //.addInterceptor(loggingInterceptor)
                    .build();

            justEatClient = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build()
                    .create(JustEatClient.class);
        }

        if (googleMapClient == null) {
            googleMapClient = new Retrofit.Builder()
                    .baseUrl(GOOGLE_API_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(new OkHttpClient.Builder().build())
                    .build()
                    .create(GoogleMapClient.class);
        }
    }

    public void getRestaurantsList(String postalCode, Callback<RestaurantsHolder> callback) {
        Call<RestaurantsHolder> messages = justEatClient.getRestaurants(postalCode);

        messages.enqueue(new retrofit2.Callback<RestaurantsHolder>() {
            @Override
            public void onResponse(@NonNull Call<RestaurantsHolder> call,
                                   @NonNull Response<RestaurantsHolder> response) {
                if (response.isSuccessful() && response.errorBody() == null) {
                    callback.onSuccess(response);
                } else {
                    String errorStr = null;
                    if (response.errorBody() != null) {
                        try {
                            errorStr = response.errorBody().string();
                        } catch (Throwable ignore) {
                        }
                    } else {
                        errorStr = response.message();
                    }
                    callback.onFailure(errorStr);
                }
            }

            @Override
            public void onFailure(@NonNull Call<RestaurantsHolder> call, @NonNull Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    public void getRestaurantsListByLocation(String latlng, Callback<RestaurantsHolder> callback) {
        Call<String> messages = googleMapClient.getGeocodeInfo(latlng);

        messages.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful() && response.errorBody() == null) {
                    String geodataResponse = response.body();
                    if (geodataResponse != null) {
                        String postalCode = getPostalCode(geodataResponse);
                        if (postalCode != null) {
                            getRestaurantsList(postalCode, callback);
                            return;
                        }
                    }
                    callback.onFailure("No postal code found");
                } else {
                    String errorStr = null;
                    if (response.errorBody() != null) {
                        try {
                            errorStr = response.errorBody().string();
                        } catch (Throwable ignore) {
                        }
                    } else {
                        errorStr = response.message();
                    }
                    callback.onFailure(errorStr);
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    private String getPostalCode(String geodataResponse) {
        try {
            JSONObject result = new JSONObject(geodataResponse);
            if (result.has("results")) {
                JSONArray array = result.getJSONArray("results");
                if (array.length() > 0) {
                    JSONObject place = array.getJSONObject(0);
                    JSONArray components = place.getJSONArray("address_components");
                    for (int i = 0; i < components.length(); i++) {
                        JSONObject component = components.getJSONObject(i);
                        JSONArray types = component.getJSONArray("types");
                        for (int j = 0; j < types.length(); j++) {
                            if (types.getString(j).equals("postal_code")) {
                                return component.getString("long_name");
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
