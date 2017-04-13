package com.tine.coffeeshops.ui.main.map;

import android.content.Context;
import android.content.res.Resources;

import com.tine.coffeeshops.api.service.PlacesApiService;
import com.tine.coffeeshops.di.scopes.ViewScope;
import com.tine.coffeeshops.rx.location.MainThreadManager;
import com.tine.coffeeshops.ui.main.map.manager.LocationManager;

import dagger.Module;
import dagger.Provides;

@Module
public class CoffeeShopsMapModule {

    private final CoffeeShopsMapMvp.View view;

    public CoffeeShopsMapModule(CoffeeShopsMapMvp.View view) {
        this.view = view;
    }

    @ViewScope @Provides CoffeeShopsMapMvp.View provideView() {
        return view;
    }

    @ViewScope @Provides CoffeeShopsMapMvp.Presenter providePresenter(CoffeeShopsMapMvp.View view,
            PlacesApiService placesApiService, Resources resources, MainThreadManager mainThreadManager,
            LocationManager locationManager) {
        return new CoffeeShopsMapPresenter(view, placesApiService, resources, mainThreadManager,
                locationManager);
    }

    @ViewScope @Provides LocationManager provideLocationManager(Context context) {
        return new LocationManager(context);
    }
}
