package com.tine.coffeeshops.ui.main.map;

import android.content.Context;

import com.tine.coffeeshops.api.service.PlacesApiService;
import com.tine.coffeeshops.di.scopes.ViewScope;

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

    @ViewScope @Provides CoffeeShopsMapMvp.Presenter providePresenter(CoffeeShopsMapMvp.View view, Context context,
            PlacesApiService placesApiService) {
        return new CoffeeShopsMapPresenter(view, context, placesApiService);
    }
}
