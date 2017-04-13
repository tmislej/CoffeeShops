package com.tine.coffeeshops.ui.main.map;

import android.content.Context;
import android.content.res.Resources;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tine.coffeeshops.api.model.PlacesResponseWrapper;
import com.tine.coffeeshops.api.service.PlacesApiService;
import com.tine.coffeeshops.rx.location.MainThreadManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;

import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CoffeeShopsMapPresenterUnitTest {

    @Mock CoffeeShopsMapMvp.View view;
    @Mock Context context;
    @Mock PlacesApiService placesApiService;
    @Mock Resources resources;
    @Mock MainThreadManager mainThreadManager;

    private CoffeeShopsMapPresenter presenter;

    private Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    @Before
    public void setUp() {
        presenter = new CoffeeShopsMapPresenter(view, context, placesApiService, resources, mainThreadManager);
    }

    @SuppressWarnings("WrongConstant") @Test
    public void testInitialLoad() {
        PlacesResponseWrapper response = gson.fromJson(JSON, PlacesResponseWrapper.class);

        when(mainThreadManager.observableToMainThread()).thenReturn(o -> o);
        when(placesApiService.getNearbyPlaces(anyDouble(), anyDouble(), anyString(),
                anyInt())).thenReturn(Observable.just(response));

        presenter.onReady();
    }

    private static final String JSON = "{\n"
            + "  \"results\": [\n"
            + "    {\n"
            + "      \"geometry\": {\n"
            + "        \"location\": {\n"
            + "          \"lat\": 46.05158000000001,\n"
            + "          \"lng\": 14.509255\n"
            + "        }\n"
            + "      },\n"
            + "      \"name\": \"Degustarna\",\n"
            + "      \"opening_hours\": {\n"
            + "        \"open_now\": true,\n"
            + "        \"weekday_text\": []\n"
            + "      }\n"
            + "    },\n"
            + "    {\n"
            + "      \"geometry\": {\n"
            + "        \"location\": {\n"
            + "          \"lat\": 46.0505212,\n"
            + "          \"lng\": 14.5122483\n"
            + "        }\n"
            + "      },\n"
            + "      \"name\": \"Stiška čajnica\",\n"
            + "      \"opening_hours\": {\n"
            + "        \"open_now\": true,\n"
            + "        \"weekday_text\": []\n"
            + "      }\n"
            + "    },\n"
            + "    {\n"
            + "      \"geometry\": {\n"
            + "        \"location\": {\n"
            + "          \"lat\": 46.05041079999999,\n"
            + "          \"lng\": 14.50839789999999\n"
            + "        }\n"
            + "      },\n"
            + "      \"name\": \"Čez Cesto\"\n"
            + "    },\n"
            + "    {\n"
            + "      \"geometry\": {\n"
            + "        \"location\": {\n"
            + "          \"lat\": 46.0520868,\n"
            + "          \"lng\": 14.5084986\n"
            + "        }\n"
            + "      },\n"
            + "      \"name\": \"Čarli J. podjetje za gostinstvo, trgovino in storitve, d.o.o. Ljubljana\"\n"
            + "    }\n"
            + "  ]\n"
            + "}";
}
