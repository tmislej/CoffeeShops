package com.tine.coffeeshops.ui.main.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.maps.android.clustering.ClusterManager;
import com.tine.coffeeshops.R;
import com.tine.coffeeshops.ui.main.MainActivity;
import com.tine.coffeeshops.ui.main.map.model.UiPlace;

import javax.inject.Inject;

public class CoffeeShopsMap extends MapView implements CoffeeShopsMapMvp.View, OnMapReadyCallback {

    @Inject CoffeeShopsMapMvp.Presenter presenter;

    private GoogleMap map;
    private Snackbar errorSnackbar;
    private Snackbar loadingSnackbar;

    public CoffeeShopsMap(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        getMapAsync(this);
        inject();
    }

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (errorSnackbar != null) {
            errorSnackbar.dismiss();
        }

        if (loadingSnackbar != null) {
            loadingSnackbar.dismiss();
        }

        presenter.onDetachedFromWindow();
    }

    @Override public void setMyLocationEnabled(boolean enabled) {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(enabled);
        }
    }

    @Override public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        ClusterManager<UiPlace> clusterManager = new ClusterManager<>(getContext(), map);

        map.setOnCameraIdleListener(clusterManager);
        map.setOnMarkerClickListener(clusterManager);

        presenter.onMapReady(clusterManager);
    }

    @Override public void showInfoSnackbar(@StringRes int res) {
        Snackbar.make(this, res, Snackbar.LENGTH_LONG).show();
    }

    @Override public void showErrorSnabckbar(@StringRes int res) {
        if (errorSnackbar != null && errorSnackbar.isShown()) {
            errorSnackbar.dismiss();
        }

        errorSnackbar = Snackbar.make(this, res, Snackbar.LENGTH_LONG);
        errorSnackbar.getView().setBackgroundResource(android.R.color.holo_red_light);
        errorSnackbar.show();
    }

    @Override public void showLoading() {
        loadingSnackbar = Snackbar.make(this, R.string.loading, Snackbar.LENGTH_INDEFINITE);
        loadingSnackbar.show();
    }

    @Override public void hideLoading() {
        loadingSnackbar.dismiss();
    }

    public void onReady() {
        presenter.onReady();
    }

    private void inject() {
        MainActivity activity = (MainActivity) getContext();
        DaggerCoffeeShopsMapComponent.builder()
                .mainComponent(activity.getComponent())
                .coffeeShopsMapModule(new CoffeeShopsMapModule(this))
                .build()
                .inject(this);
    }
}
