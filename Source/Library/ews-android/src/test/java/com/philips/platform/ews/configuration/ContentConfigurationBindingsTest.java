/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.platform.ews.configuration;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.platform.ews.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest(TextUtils.class)
public class ContentConfigurationBindingsTest {

    @Mock
    TextView mockTextView;
    @Mock
    ImageView mockImageView;
    @Mock
    Context mMockContext;
    @Mock
    BaseContentConfiguration mockBaseContentConfiguration;

    final String APPNAME = "SleepMapper";
    final String DEVICENAME = "DeviceSleeping";

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.when(TextUtils.isEmpty(any(CharSequence.class))).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                CharSequence a = (CharSequence) invocation.getArguments()[0];
                return !(a != null && a.length() > 0);
            }
        });
        when(mockTextView.getContext()).thenReturn(mMockContext);
        when(mockBaseContentConfiguration.getAppName()).thenReturn(123234);
        when(mockBaseContentConfiguration.getDeviceName()).thenReturn(45345345);
        when(mMockContext.getString(anyInt())).thenReturn(APPNAME);
    }

    @Test
    public void itShouldCheckWhenSetTextFormatterCallWithOneArg() throws Exception {
        String formatedString = String.format(mMockContext.getString(R.string.label_ews_get_started_title), APPNAME);
        ContentConfigurationBindings.setFormattedText(mockTextView, mMockContext.getString(R.string.label_ews_get_started_title), mockBaseContentConfiguration.getAppName());
        verify(mockTextView).setText(formatedString);
    }

    @Test
    public void itShouldFailWhenAppNameResourceNotSetTextFormatterCallWithOneArg() throws Exception {
        when(mockBaseContentConfiguration.getAppName()).thenReturn(0);
        ContentConfigurationBindings.setFormattedText(mockTextView, mMockContext.getString(R.string.label_ews_get_started_title), mockBaseContentConfiguration.getAppName());
        verify(mockTextView, times(0)).setText(anyString());
    }

    @Test
    public void itShouldSuccessWhenImageResourceSet() throws Exception {
        ContentConfigurationBindings.setEWSImageResource(mockImageView, R.drawable.ic_ews_device_apmode_blinking);
        verify(mockImageView).setImageResource(anyInt());
    }

    @Test
    public void itShouldFailWhenImageResourceNotSet() throws Exception {
        ContentConfigurationBindings.setEWSImageResource(mockImageView, 0);
        verify(mockImageView, times(0)).setImageResource(anyInt());
    }

    @Test
    public void itShouldCheckWhenSetTextFormatterCallWithTwoArgs() throws Exception {
        String formatedString = String.format(mMockContext.getString(R.string.label_ews_confirm_connection_tip_upper), DEVICENAME, APPNAME);
        ContentConfigurationBindings.setFormattedText(mockTextView, mMockContext.getString(R.string.label_ews_confirm_connection_tip_upper), mockBaseContentConfiguration.getDeviceName(), mockBaseContentConfiguration.getDeviceName());
        verify(mockTextView).setText(formatedString);
    }

    @Test
    public void itShouldFailWhenArg1NotSet() throws Exception {
        when(mockBaseContentConfiguration.getDeviceName()).thenReturn(0);
        ContentConfigurationBindings.setFormattedText(mockTextView, mMockContext.getString(R.string.label_ews_confirm_connection_tip_upper), mockBaseContentConfiguration.getDeviceName(), mockBaseContentConfiguration.getAppName());
        verify(mockTextView, times(0)).setText(anyString());
    }

    @Test
    public void itShouldFailWhenArg2NotSet() throws Exception {
        when(mockBaseContentConfiguration.getAppName()).thenReturn(0);
        ContentConfigurationBindings.setFormattedText(mockTextView, mMockContext.getString(R.string.label_ews_confirm_connection_tip_upper), mockBaseContentConfiguration.getDeviceName(), mockBaseContentConfiguration.getAppName());
        verify(mockTextView, times(0)).setText(anyString());
    }

    @Test
    public void itShouldFailWhenBothArgNotSet() throws Exception {
        when(mockBaseContentConfiguration.getDeviceName()).thenReturn(0);
        when(mockBaseContentConfiguration.getAppName()).thenReturn(0);
        ContentConfigurationBindings.setFormattedText(mockTextView, mMockContext.getString(R.string.label_ews_confirm_connection_tip_upper), mockBaseContentConfiguration.getDeviceName(), mockBaseContentConfiguration.getAppName());
        verify(mockTextView, times(0)).setText(anyString());
    }


    @Test
    public void itShouldCheckWhenSetStringUsingStringFormatter() throws Exception {
        String formatedString = String.format(mMockContext.getString(R.string.label_ews_get_started_title), APPNAME);
        ContentConfigurationBindings.setStringFormattedText(mockTextView, mMockContext.getString(R.string.label_ews_get_started_title), APPNAME);
        verify(mockTextView).setText(formatedString);
    }

    @Test
    public void itShouldFailWhenEmptyStringPassedINSetStringUsingStringFormatter() throws Exception {
        ContentConfigurationBindings.setStringFormattedText(mockTextView, mMockContext.getString(R.string.label_ews_get_started_title), "");
        verify(mockTextView, times(0)).setText(anyString());
    }

    @Test
    public void itShouldCheckWhenSetStringUsingStringFormatterWithResourceId() throws Exception {
        String formatedString = String.format(mMockContext.getString(R.string.label_ews_confirm_connection_tip_upper), DEVICENAME, APPNAME);
        ContentConfigurationBindings.setStringFormattedText(mockTextView, mMockContext.getString(R.string.label_ews_confirm_connection_tip_upper), mockBaseContentConfiguration.getDeviceName(), APPNAME);
        verify(mockTextView).setText(formatedString);
    }

    @Test
    public void itShouldFailWhenResourceIdNotaddedandDeviceNameAvailable() throws Exception {
        when(mockBaseContentConfiguration.getDeviceName()).thenReturn(0);
        ContentConfigurationBindings.setStringFormattedText(mockTextView, mMockContext.getString(R.string.label_ews_confirm_connection_tip_upper), mockBaseContentConfiguration.getDeviceName(), APPNAME);
        verify(mockTextView, times(0)).setText(anyString());
    }

    @Test
    public void itShouldFailWhenResourceIdAvailableAndAppnameIsEmpty() throws Exception {
        ContentConfigurationBindings.setStringFormattedText(mockTextView, mMockContext.getString(R.string.label_ews_confirm_connection_tip_upper), mockBaseContentConfiguration.getDeviceName(), "");
        verify(mockTextView, times(0)).setText(anyString());
    }

    @Test
    public void itShouldFailWhenResourceIdAndDeviceNameEmpty() throws Exception {
        when(mockBaseContentConfiguration.getDeviceName()).thenReturn(0);
        ContentConfigurationBindings.setStringFormattedText(mockTextView, mMockContext.getString(R.string.label_ews_confirm_connection_tip_upper), mockBaseContentConfiguration.getDeviceName(), "");
        verify(mockTextView, times(0)).setText(anyString());
    }
}