/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.philips.platform.uid.view.widget.Label;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.Mockito.when;

/**
 * Created by philips on 9/27/17.
 */
@RunWith(RobolectricTestRunner.class)
@PrepareForTest(Resources.Theme.class)
public class UIPickerAdapterTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Mock
    private Context contextMock;

    @Mock
    private LayoutInflater inflaterMock;

    @Mock
    private Resources.Theme themeMock;

    @Mock
    private TypedArray typedArrayMock;

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

        when(typedArrayMock.getString(com.philips.platform.uid.R.styleable.PhilipsUID_uidTonalRange)).thenReturn("VeryDarkKK");

        when(themeMock.obtainStyledAttributes(com.philips.platform.uid.R.styleable.PhilipsUID)).thenReturn(typedArrayMock);

        when(contextMock.getTheme()).thenReturn(themeMock);
        when(contextMock.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).thenReturn(inflaterMock);
        uiPickerAdapter = new UIPickerAdapter(contextMock, com.philips.platform.uid.R.id.action_bar_title, assets);
    }

    @Test
    public void getView() throws Exception {
        uiPickerAdapter.getView(0,viewMock,viewGroupMock);
    }

    @Test(expected = NullPointerException.class)
    public void getViewWhenConvertViewIsNull() throws Exception {

        when(inflaterMock.inflate(0, viewGroupMock, false)).thenReturn(viewMock);
        uiPickerAdapter.getView(0,null,viewGroupMock);
    }

}