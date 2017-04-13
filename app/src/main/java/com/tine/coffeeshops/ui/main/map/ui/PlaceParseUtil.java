package com.tine.coffeeshops.ui.main.map.ui;


import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.tine.coffeeshops.R;
import com.tine.coffeeshops.api.model.OpeningHoursResponse;
import com.tine.coffeeshops.api.model.PlaceResponse;
import com.tine.coffeeshops.ui.main.map.model.UiPlace;

public final class PlaceParseUtil {

    private PlaceParseUtil(){
        // no instance
    }

    public static UiPlace parsePlace(@NonNull PlaceResponse placeResponse, @NonNull Resources resources){
        double latitude = placeResponse.getGeometry().getLocation().getLat();
        double longitude = placeResponse.getGeometry().getLocation().getLng();
        String name = placeResponse.getName();

        OpeningHoursResponse openingHours = placeResponse.getOpeningHours();
        String info;
        if (openingHours != null) {
            info = resources.getString(openingHours.isOpenNow() ? R.string.place_opened : R.string.place_closed);
        } else {
            info = resources.getString(R.string.place_opening_hours_unknown);
        }

        return new UiPlace(latitude, longitude, name, info);
    }
}
