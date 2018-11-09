/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.adapters;

import android.app.Application;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.philips.cdp.di.iap.R;
import com.philips.platform.uid.view.widget.Label;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.mockito.Mockito.when;

/**
 * Created by philips on 9/27/17.
 */
@RunWith(RobolectricTestRunner.class)
@Config(application = UIPickerAdapterTest.TestApplication.class)
public class UIPickerAdapterTest {

    @Mock
    private LayoutInflater inflaterMock;

    @Mock
    private Label viewMock;

    @Mock
    private ViewGroup viewGroupMock;

    private UIPickerAdapter uiPickerAdapter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        String[] assets = new String[10];
        assets[0] = "Cheese";

        uiPickerAdapter = new UIPickerAdapter(getInstrumentation().getTargetContext(), com.philips.platform.uid.R.id.action_bar_title, assets);
    }

    @Test
    public void getView() throws Exception {
        uiPickerAdapter.getView(0, viewMock, viewGroupMock);
    }

    @Test(expected = Resources.NotFoundException.class)
    public void getViewWhenConvertViewIsNull() throws Exception {
        when(inflaterMock.inflate(0, viewGroupMock, false)).thenReturn(viewMock);
        uiPickerAdapter.getView(0, null, viewGroupMock);
    }

    public static class TestApplication extends Application {

        @Override
        public void onCreate() {
            super.onCreate();
            setTheme(R.style.AppTheme);
        }
    }
}

