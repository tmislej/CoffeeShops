package com.tine.coffeeshops.ui.main;

import android.Manifest;
import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class MainPresenterUnitTest {

    @Mock MainMvp.View view;
    @Mock Context context;

    MainPresenter presenter;

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

        assertEquals(captor.getValue(), Manifest.permission.ACCESS_FINE_LOCATION);
        verifyNoMoreInteractions(view);
    }
}
