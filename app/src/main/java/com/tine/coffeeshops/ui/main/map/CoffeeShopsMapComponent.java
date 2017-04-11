package com.tine.coffeeshops.ui.main.map;

import com.tine.coffeeshops.di.scopes.ViewScope;
import com.tine.coffeeshops.ui.main.MainComponent;

import dagger.Component;

@ViewScope
@Component(dependencies = MainComponent.class, modules = CoffeeShopsMapModule.class)
public interface CoffeeShopsMapComponent {

    void inject(CoffeeShopsMap view);
}
