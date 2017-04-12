package com.tine.coffeeshops.ui.main.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.tine.coffeeshops.ui.main.MainActivity;

import javax.inject.Inject;

public class CoffeeShopsMap extends MapView implements CoffeeShopsMapMvp.View, OnMapReadyCallback {

    @Inject CoffeeShopsMapMvp.Presenter presenter;
    private GoogleMap map;

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
        presenter.onDetachedFromWindow();
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

    @Override public void onMapReady(GoogleMap googleMap) {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map = googleMap;
            map.setMyLocationEnabled(true);

            presenter.onMapReady(map);
        }
    }
}
