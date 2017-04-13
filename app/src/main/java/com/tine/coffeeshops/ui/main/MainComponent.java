package com.tine.coffeeshops.ui.main;

import android.content.Context;
import android.content.res.Resources;

import com.tine.coffeeshops.AppComponent;
import com.tine.coffeeshops.api.service.PlacesApiService;
import com.tine.coffeeshops.di.scopes.ActivityScope;
import com.tine.coffeeshops.rx.location.MainThreadManager;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = MainModule.class)
public interface MainComponent {

    void inject(MainActivity activity);

    Context context();

    Resources resources();

    PlacesApiService placesApiService();

    MainThreadManager mainThreadManager();
}
