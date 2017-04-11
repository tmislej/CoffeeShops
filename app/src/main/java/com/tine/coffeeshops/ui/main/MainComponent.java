package com.tine.coffeeshops.ui.main;

import android.content.Context;

import com.tine.coffeeshops.AppComponent;
import com.tine.coffeeshops.di.scopes.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = MainModule.class)
public interface MainComponent {

    void inject(MainActivity activity);

    Context context();
}
