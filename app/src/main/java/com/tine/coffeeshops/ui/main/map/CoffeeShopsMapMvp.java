package com.tine.coffeeshops.ui.main.map;

public interface CoffeeShopsMapMvp {

    interface View {

    }

    interface Presenter {

        void onReady();

        void onDetachedFromWindow();

        void onMapReady();
    }
}
