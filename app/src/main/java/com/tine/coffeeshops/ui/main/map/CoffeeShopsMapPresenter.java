package com.tine.coffeeshops.ui.main.map;

import android.content.Context;
import android.location.Location;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tine.coffeeshops.rx.location.LocationObservable;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CoffeeShopsMapPresenter implements CoffeeShopsMapMvp.Presenter {

    private final CoffeeShopsMapMvp.View view;
    private final Context context;

    private boolean isMapReady = false;
    private Subscription locationSubscription;

    public CoffeeShopsMapPresenter(CoffeeShopsMapMvp.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override public void onReady() {
        locationSubscription = Observable.create(new LocationObservable(context))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Location>() {
                    @Override public void onCompleted() {

                    }

                    @Override public void onError(Throwable e) {

                    }

                    @Override public void onNext(@Nullable Location location) {
                        if (location != null && isMapReady) {
                            Log.d("LOCATION",
                                    String.format("%s - %s", location.getLatitude(), location.getLongitude()));
                        }
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
