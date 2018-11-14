package com.justeat.vasylt.testtask.screen.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.justeat.vasylt.testtask.R;
import com.justeat.vasylt.testtask.app.App;
import com.justeat.vasylt.testtask.model.Restaurant;
import com.justeat.vasylt.testtask.screen.BaseActivity;
import com.justeat.vasylt.testtask.screen.main.di.MainActivityComponent;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.justeat.vasylt.testtask.Constants.PERMISSION_LOCATION;
import static com.justeat.vasylt.testtask.Constants.REQUEST_CHECK_SETTINGS;

/**
 * Created by Vasyl Tkachov on 26.06.2018.
 */
public class MainActivity extends BaseActivity implements MainView {

    @Inject
    MainActivityPresenter presenter;

    @BindView(R.id.list_view)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.no_data_placeholder)
    TextView vNoData;

    private RestaurantsListAdapter      cardAdapter;
    private FusedLocationProviderClient locationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        MainActivityComponent component = (MainActivityComponent) App.getApp(this)
                .getComponentsHolder()
                .getActivityComponent(getClass());
        component.inject(this);

        setSupportActionBar(toolbar);

        vNoData.setOnClickListener(view -> {
        });

        setUpList();
        presenter.onAttach(this);
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
        if (isFinishing()) {
            App.getApp(this).getComponentsHolder().releaseActivityComponent(getClass());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onLocationPicked(String postalCode) {
        collapsingToolbar.setTitle(postalCode);
    }

    @Override
    public void onRestaurantsLoaded(List<Restaurant> itemList) {
        Log.d("justeat", "onRestaurantsLoaded: " + itemList.size());
        hideLoading();
        cardAdapter.setItems(itemList);
        if (!isFinishing()) {
            vNoData.setVisibility(cardAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onLoadFail(String error) {
        hideLoading();
        showMessage("onLoadFail: " + error);
        cardAdapter.clear();
    }

    @OnClick(R.id.set_postal_code)
    @SuppressLint("InflateParams")
    public void showPostalCodePicker() {
        if (!isNetworkConnected()) {
            showMessage(getString(R.string.network_unavailable));
            return;
        }

        Dialog dialog = new Dialog(this);
        View rootView = LayoutInflater.from(this).inflate(R.layout.dialog_postal_code_picker,
                null, false);
        dialog.setContentView(rootView);

        TextInputLayout etInputLayout = rootView.findViewById(R.id.postal_code_input_layout);
        TextInputEditText etInput = rootView.findViewById(R.id.postal_code_input);
        TextView btnConfirm = rootView.findViewById(R.id.confirm_button);
        TextView btnGetPostcode = rootView.findViewById(R.id.get_postcode);

        etInput.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            switch (actionId) {
                case EditorInfo.IME_ACTION_DONE:
                    String postalCodeValue = etInput.getText().toString();
                    setPostalCode(postalCodeValue, etInputLayout, dialog);
                    break;
            }
            return false;
        });

        btnGetPostcode.setOnClickListener(view -> {
            getLastLocation();
            dialog.dismiss();
        });

        btnConfirm.setOnClickListener(view -> {
            String postalCodeValue = etInput.getText().toString();
            setPostalCode(postalCodeValue, etInputLayout, dialog);
        });

        dialog.create();
        dialog.show();
    }

    private void setUpList() {
        cardAdapter = new RestaurantsListAdapter(this, view -> {
            // TODO: add action on item click
        });

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(cardAdapter);
    }

    private void setPostalCode(String postalCode, TextInputLayout inputLayout, Dialog dialog) {
        if (TextUtils.isEmpty(postalCode)) {
            inputLayout.setError(getString(R.string.postal_code_empty));
        } else {
            showLoading(getString(R.string.get_restaurants_progress));
            presenter.getRestaurantsList(postalCode);
            dialog.dismiss();
        }
    }

    private void getNearbyRestaurants(Location location) {
        if (location != null) {
            presenter.getRestaurantsList(location.getLatitude(), location.getLongitude());
        } else {
            hideLoading();
            showMessage(getString(R.string.location_unknown));
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (hasLocationPermission()) {
            if (isLocationEnabled()) {
                showLoading(getString(R.string.fetch_location_progress));
                if (locationClient == null) {
                    locationClient = LocationServices.getFusedLocationProviderClient(this);
                }
                locationClient.getLastLocation().addOnSuccessListener(this, this::getNearbyRestaurants);
            } else {
                displayLocationSettingsRequest();
            }
        }
    }

    private boolean hasLocationPermission() {
        return hasPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                PERMISSION_LOCATION, R.string.permission_location_rationale);
    }

    private boolean isLocationEnabled() {
        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locManager != null && locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @SuppressWarnings("deprecation") 
	private void displayLocationSettingsRequest() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                .checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(result1 -> {
            final Status status = result1.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    Log.i("justeat", "Location enabled");
                    getLastLocation();
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    Log.i("justeat", "Show dialog to upgrade location settings");
                    try {
                        status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException e) {
                        Log.e("justeat", "PendingIntent unable to execute request");
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    showMessage(getString(R.string.location_broken));
                    Log.wtf("justeat", getString(R.string.location_broken));
                    break;
            }
        });
    }
}