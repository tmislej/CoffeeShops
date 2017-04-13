package com.tine.coffeeshops.ui.main.map;

import android.support.annotation.StringRes;

import com.google.maps.android.clustering.ClusterManager;
import com.tine.coffeeshops.ui.main.map.model.UiPlace;

public interface CoffeeShopsMapMvp {

    interface View {

        void showInfoSnackbar(@StringRes int res);

        void showErrorSnabckbar(@StringRes int res);

        void setMyLocationEnabled(boolean enabled);

        void showLoading();

        void hideLoading();
    }

    interface Presenter {

        void onReady();

        void onDetachedFromWindow();

        void onMapReady(ClusterManager<UiPlace> clusterManager);
    }
}
