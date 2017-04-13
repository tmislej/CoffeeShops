package com.tine.coffeeshops.ui.main.map.ui;

import android.content.res.Resources;

import com.google.android.gms.maps.model.LatLng;
import com.tine.coffeeshops.R;
import com.tine.coffeeshops.api.model.GeometryResponse;
import com.tine.coffeeshops.api.model.LocationResponse;
import com.tine.coffeeshops.api.model.OpeningHoursResponse;
import com.tine.coffeeshops.api.model.PlaceResponse;
import com.tine.coffeeshops.ui.main.map.model.UiPlace;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class PlaceParseUtilTest {

    @Mock Resources resources;
    @Mock PlaceResponse response;
    @Mock GeometryResponse geometry;
    @Mock LocationResponse location;
    @Mock OpeningHoursResponse openingHours;

    @Before
    public void setUp() {
        when(location.getLat()).thenReturn(11.1);
        when(location.getLng()).thenReturn(22.2);
        when(geometry.getLocation()).thenReturn(location);
        when(response.getGeometry()).thenReturn(geometry);
        when(response.getName()).thenReturn("foo");
        when(openingHours.isOpenNow()).thenReturn(true);
        when(response.getOpeningHours()).thenReturn(openingHours);
        when(resources.getString(R.string.place_opened)).thenReturn("opened");
        when(resources.getString(R.string.place_closed)).thenReturn("closed");
        when(resources.getString(R.string.place_opening_hours_unknown)).thenReturn("unknown");
    }

    @Test
    public void testParsePlaceOpened() {
        UiPlace place = PlaceParseUtil.parsePlace(response, resources);
        assertEquals(place.getTitle(), "foo");
        assertEquals(place.getPosition(), new LatLng(11.1, 22.2));
        assertEquals(place.getSnippet(), "opened");
    }

    @Test
    public void testParsePlaceClosed() {
        when(openingHours.isOpenNow()).thenReturn(false);

        UiPlace place = PlaceParseUtil.parsePlace(response, resources);
        assertEquals(place.getTitle(), "foo");
        assertEquals(place.getPosition(), new LatLng(11.1, 22.2));
        assertEquals(place.getSnippet(), "closed");
    }

    @Test
    public void testParsePlaceUnknownOpeningHours() {
        when(response.getOpeningHours()).thenReturn(null);

        UiPlace place = PlaceParseUtil.parsePlace(response, resources);
        assertEquals(place.getTitle(), "foo");
        assertEquals(place.getPosition(), new LatLng(11.1, 22.2));
        assertEquals(place.getSnippet(), "unknown");
    }
}
