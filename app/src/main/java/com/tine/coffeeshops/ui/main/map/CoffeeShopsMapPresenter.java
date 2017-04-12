package com.tine.coffeeshops.ui.main.map;

import android.content.Context;
import android.support.annotation.Nullable;

import com.tine.coffeeshops.api.model.PlacesResponseWrapper;
import com.tine.coffeeshops.api.service.PlacesApiService;
import com.tine.coffeeshops.rx.location.LocationObservable;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CoffeeShopsMapPresenter implements CoffeeShopsMapMvp.Presenter {

    private static final int RADIUS = 500;

    private final CoffeeShopsMapMvp.View view;
    private final Context context;
    private final PlacesApiService placesApiService;

    private boolean isMapReady = false;
    private Subscription locationSubscription;

    public CoffeeShopsMapPresenter(CoffeeShopsMapMvp.View view, Context context, PlacesApiService placesApiService) {
        this.view = view;
        this.context = context;
        this.placesApiService = placesApiService;
    }

    @Override public void onReady() {
        locationSubscription = Observable.create(new LocationObservable(context))
                .flatMap(location -> placesApiService.getNearbyPlaces(location.getLatitude(), location.getLongitude(),
                        PlacesApiService.TYPE_CAFE, RADIUS))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PlacesResponseWrapper>() {
                    @Override public void onCompleted() {

                    }

                    @Override public void onError(Throwable e) {
                        // TODO: 12/04/17 impl. 

                    }

                    @Override public void onNext(@Nullable PlacesResponseWrapper location) {
                        // TODO: 12/04/17 impl. 
                    }
                });
    }

    @Override public void onMapReady() {
        isMapReady = true;
    }

    @Override public void onDetachedFromWindow() {
        if (locationSubscription != null && !locationSubscription.isUnsubscribed()) {
            locationSubscription.unsubscribe();
        }
    }
}
