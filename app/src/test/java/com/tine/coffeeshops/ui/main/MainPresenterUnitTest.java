package com.tine.coffeeshops.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import com.tine.coffeeshops.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class MainPresenterUnitTest {

    @Mock MainMvp.View view;
    @Mock Context context;

    private MainPresenter presenter;

    @Before
    public void setUp() {
        presenter = new MainPresenter(view, context);
    }

    @Test
    public void testOnRationaleNext() {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        presenter.onRationaleNext();
        verify(view).hideRationaleDialog();
        verify(view).requestPermission(captor.capture());
        verifyNoMoreInteractions(view);

        assertEquals(captor.getValue(), Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @Test public void testOnRequestPermissionsResultGranted() {
        presenter.onRequestPermissionsResult(MainActivity.REQUEST_LOCATION_PERMISSION,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                new int[]{PackageManager.PERMISSION_GRANTED});

        verify(view).onReady();
        verifyNoMoreInteractions(view);
    }

    @Test public void testOnRequestPermissionsResultDenied() {
        presenter.onRequestPermissionsResult(MainActivity.REQUEST_LOCATION_PERMISSION,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                new int[]{PackageManager.PERMISSION_DENIED});

        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);

        verify(view, never()).onReady();
        verify(view).showToast(captor.capture());
        verifyNoMoreInteractions(view);

        assertEquals(captor.getValue().intValue(), R.string.no_permission);
    }

    @Test(expected = IllegalStateException.class) public void testOnRequestPermissionsResultUnknownRequestCode() {
        presenter.onRequestPermissionsResult(Integer.MIN_VALUE, new String[0], new int[0]);
    }
}
