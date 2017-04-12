package com.tine.coffeeshops.ui.main.map;

import com.google.android.gms.maps.GoogleMap;

public interface CoffeeShopsMapMvp {

    interface View {

    }

    interface Presenter {

        void onReady();

        void onDetachedFromWindow();

        void onMapReady(GoogleMap map);
    }
}
