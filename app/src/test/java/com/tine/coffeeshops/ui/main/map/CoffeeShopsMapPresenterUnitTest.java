package com.tine.coffeeshops.ui.main.map;

import android.accounts.NetworkErrorException;
import android.content.res.Resources;
import android.location.Location;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.android.clustering.ClusterManager;
import com.tine.coffeeshops.R;
import com.tine.coffeeshops.api.model.PlacesResponseWrapper;
import com.tine.coffeeshops.api.service.PlacesApiService;
import com.tine.coffeeshops.rx.location.MainThreadManager;
import com.tine.coffeeshops.ui.main.map.manager.LocationManager;
import com.tine.coffeeshops.ui.main.map.model.UiPlace;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import rx.Observable;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CoffeeShopsMapPresenterUnitTest {

    @Mock CoffeeShopsMapMvp.View view;
    @Mock PlacesApiService placesApiService;
    @Mock Resources resources;
    @Mock MainThreadManager mainThreadManager;
    @Mock LocationManager locationManager;
    @Mock Location location;

    private CoffeeShopsMapPresenter presenter;

    private Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    @Before
    public void setUp() {
        presenter = new CoffeeShopsMapPresenter(view, placesApiService, resources, mainThreadManager,
                locationManager);

        when(location.getLatitude()).thenReturn(44.1);
        when(location.getLongitude()).thenReturn(14.1);
    }

    @Test
    public void testInitialLoad() {
        PlacesResponseWrapper response = gson.fromJson(JSON, PlacesResponseWrapper.class);
        when(locationManager.requestLocationUpdates()).thenReturn(Observable.just(location));
        @SuppressWarnings("unchecked") ClusterManager<UiPlace> clusterManager = mock(ClusterManager.class);

        when(mainThreadManager.observableToMainThread()).thenReturn(o -> o);

        //noinspection WrongConstant
        when(placesApiService.getNearbyPlaces(anyDouble(), anyDouble(), anyString(),
                anyInt())).thenReturn(Observable.just(response));

        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);

        presenter.onMapReady(clusterManager);
        presenter.onReady();

        verify(view).showLoading();
        verify(view, times(2)).setMyLocationEnabled(anyBoolean());
        verify(clusterManager).clearItems();
        //noinspection unchecked
        verify(clusterManager).addItems(captor.capture());
        verify(clusterManager).cluster();
        verify(view).hideLoading();
        verifyNoMoreInteractions(clusterManager);
        verifyNoMoreInteractions(view);

        assertEquals(captor.getValue().size(), 4);
    }

    @Test
    public void testInitialLoadNetworkFail() {
        @SuppressWarnings("unchecked") ClusterManager<UiPlace> clusterManager = mock(ClusterManager.class);

        when(mainThreadManager.observableToMainThread()).thenReturn(o -> o);
        when(locationManager.requestLocationUpdates()).thenReturn(Observable.just(location));

        //noinspection WrongConstant
        when(placesApiService.getNearbyPlaces(anyDouble(), anyDouble(), anyString(),
                anyInt())).thenReturn(Observable.error(new NetworkErrorException("error")));

        presenter.onMapReady(clusterManager);
        presenter.onReady();

        verify(view, times(2)).setMyLocationEnabled(anyBoolean());
        verify(view).showLoading();
        verifyZeroInteractions(clusterManager);
        verifyNoMoreInteractions(view);
    }

    @Test
    public void testInitialLocationFetchFail() {
        PlacesResponseWrapper response = gson.fromJson(JSON, PlacesResponseWrapper.class);
        @SuppressWarnings("unchecked") ClusterManager<UiPlace> clusterManager = mock(ClusterManager.class);
        when(locationManager.requestLocationUpdates()).thenReturn(
                Observable.error(new IllegalStateException("location fail")));
        when(mainThreadManager.observableToMainThread()).thenReturn(o -> o);

        //noinspection WrongConstant
        when(placesApiService.getNearbyPlaces(anyDouble(), anyDouble(), anyString(),
                anyInt())).thenReturn(Observable.just(response));

        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);

        presenter.onMapReady(clusterManager);
        presenter.onReady();

        verify(view, times(2)).setMyLocationEnabled(anyBoolean());
        verify(view).showLoading();
        verify(view).showErrorSnabckbar(captor.capture());
        verify(view).hideLoading();
        verifyNoMoreInteractions(view);
        verifyZeroInteractions(clusterManager);

        assertEquals(captor.getValue().intValue(), R.string.error_data);
    }

    @Test public void testNoPlaces() {
        PlacesResponseWrapper wrapper = mock(PlacesResponseWrapper.class);
        when(wrapper.getResults()).thenReturn(Collections.emptyList());

        when(mainThreadManager.observableToMainThread()).thenReturn(o -> o);
        when(locationManager.requestLocationUpdates()).thenReturn(Observable.just(location));
        //noinspection WrongConstant
        when(placesApiService.getNearbyPlaces(anyDouble(), anyDouble(), anyString(),
                anyInt())).thenReturn(Observable.just(wrapper));

        @SuppressWarnings("unchecked") ClusterManager<UiPlace> clusterManager = mock(ClusterManager.class);
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);

        presenter.onMapReady(clusterManager);
        presenter.onReady();

        verify(view, times(2)).setMyLocationEnabled(anyBoolean());
        verify(view).showLoading();
        verify(view).showInfoSnackbar(captor.capture());
        verify(view).hideLoading();
        verifyNoMoreInteractions(view);
        verifyZeroInteractions(clusterManager);

        assertEquals(captor.getValue().intValue(), R.string.no_data);
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
