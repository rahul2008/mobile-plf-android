package com.philips.cdp.di.iap.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.platform.uid.view.widget.Label;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * Created by philips on 9/27/17.
 */
public class UIPickerAdapterTest {

    @Mock
    Context contextMock;

    UIPickerAdapter uiPickerAdapter;

    String[] assets;

    @Mock
    private LayoutInflater inflaterMock;

    @Mock
    Resources.Theme themeMock;

    @Mock
    TypedArray typedArrayMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        assets=new String[10];
        assets[0] = "Cheese";

        when(typedArrayMock.getString(com.philips.platform.uid.R.styleable.PhilipsUID_uidTonalRange)).thenReturn("VeryDarkKK");

        when(themeMock.obtainStyledAttributes(com.philips.platform.uid.R.styleable.PhilipsUID)).thenReturn(typedArrayMock);

        when(contextMock.getTheme()).thenReturn(themeMock);
        when(contextMock.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).thenReturn(inflaterMock);
        uiPickerAdapter=new UIPickerAdapter(contextMock, com.philips.platform.uid.R.id.action_bar_title,assets);
    }

    @Mock
    Label viewMock;

    @Mock
    ViewGroup viewGroupMock;

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