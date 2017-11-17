/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.configuration;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp2.ews.R;

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
    @Mock
    ContentConfigurationBindings subject;

    final String STARTED_TITLE = "Let’s connect your %1$s to your WiFi so you can enjoy all the app enabled features!";
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

        subject = new ContentConfigurationBindings();
        when(mockTextView.getContext()).thenReturn(mMockContext);
        when(mockBaseContentConfiguration.getAppName()).thenReturn(123234);
        when(mockBaseContentConfiguration.getDeviceName()).thenReturn(45345345);
        when(mMockContext.getString(anyInt())).thenReturn(APPNAME);
    }

    @Test
    public void itShouldCheckWhenSetTextFormatterCallWithOneArg() throws Exception {
        String formatedString = String.format(mMockContext.getString(R.string.label_ews_get_started_title_default), APPNAME);
        subject.setFormattedText(mockTextView, mMockContext.getString(R.string.label_ews_get_started_title_default), mockBaseContentConfiguration.getAppName());
        verify(mockTextView).setText(formatedString);
    }

    @Test
    public void itShouldFailWhenAppNameResourceNotSetTextFormatterCallWithOneArg() throws Exception {
        when(mockBaseContentConfiguration.getAppName()).thenReturn(0);
        subject.setFormattedText(mockTextView, mMockContext.getString(R.string.label_ews_get_started_title_default), mockBaseContentConfiguration.getAppName());
        verify(mockTextView, times(0)).setText(anyString());
    }

    @Test
    public void itShouldSuccessWhenImageResourceSet() throws Exception {
        subject.setEWSImageResource(mockImageView, R.drawable.ic_ews_device_apmode_blinking);
        verify(mockImageView).setImageResource(anyInt());
    }

    @Test
    public void itShouldFailWhenImageResourceNotSet() throws Exception {
        subject.setEWSImageResource(mockImageView, 0);
        verify(mockImageView, times(0)).setImageResource(anyInt());
    }

    @Test
    public void itShouldCheckWhenSetTextFormatterCallWithTwoArgs() throws Exception {
        String formatedString = String.format(mMockContext.getString(R.string.label_ews_confirm_connection_tip_upper), DEVICENAME, APPNAME);
        subject.setFormattedText(mockTextView, mMockContext.getString(R.string.label_ews_confirm_connection_tip_upper), mockBaseContentConfiguration.getDeviceName());
        verify(mockTextView).setText(formatedString);
    }

    @Test
    public void itShouldFailWhenArg1NotSet() throws Exception {
        when(mockBaseContentConfiguration.getDeviceName()).thenReturn(0);
        subject.setFormattedText(mockTextView, mMockContext.getString(R.string.label_ews_confirm_connection_tip_upper), mockBaseContentConfiguration.getDeviceName(), mockBaseContentConfiguration.getAppName());
        verify(mockTextView, times(0)).setText(anyString());
    }

    @Test
    public void itShouldFailWhenArg2NotSet() throws Exception {
        when(mockBaseContentConfiguration.getAppName()).thenReturn(0);
        subject.setFormattedText(mockTextView, mMockContext.getString(R.string.label_ews_confirm_connection_tip_upper), mockBaseContentConfiguration.getDeviceName(), mockBaseContentConfiguration.getAppName());
        verify(mockTextView, times(0)).setText(anyString());
    }

    @Test
    public void itShouldFailWhenBothArgNotSet() throws Exception {
        when(mockBaseContentConfiguration.getDeviceName()).thenReturn(0);
        when(mockBaseContentConfiguration.getAppName()).thenReturn(0);
        subject.setFormattedText(mockTextView, mMockContext.getString(R.string.label_ews_confirm_connection_tip_upper), mockBaseContentConfiguration.getDeviceName(), mockBaseContentConfiguration.getAppName());
        verify(mockTextView, times(0)).setText(anyString());
    }


    @Test
    public void itShouldCheckWhenSetStringUsingStringFormatter() throws Exception {
        String formatedString = String.format(mMockContext.getString(R.string.label_ews_get_started_title_default), APPNAME);
        subject.setStringFormattedText(mockTextView, mMockContext.getString(R.string.label_ews_get_started_title_default), APPNAME);
        verify(mockTextView).setText(formatedString);
    }

    @Test
    public void itShouldFailWhenEmptyStringPassedINSetStringUsingStringFormatter() throws Exception {
        subject.setStringFormattedText(mockTextView, mMockContext.getString(R.string.label_ews_get_started_title_default), "");
        verify(mockTextView, times(0)).setText(anyString());
    }

    @Test
    public void itShouldCheckWhenSetStringUsingStringFormatterWithResourceId() throws Exception {
        String formatedString = String.format(mMockContext.getString(R.string.label_ews_confirm_connection_tip_upper), DEVICENAME, APPNAME);
        subject.setStringFormattedText(mockTextView, mMockContext.getString(R.string.label_ews_confirm_connection_tip_upper), mockBaseContentConfiguration.getDeviceName(), APPNAME);
        verify(mockTextView).setText(formatedString);
    }

    @Test
    public void itShouldFailWhenResourceIdNotaddedandDeviceNameAvailable() throws Exception {
        when(mockBaseContentConfiguration.getDeviceName()).thenReturn(0);
        subject.setStringFormattedText(mockTextView, mMockContext.getString(R.string.label_ews_confirm_connection_tip_upper), mockBaseContentConfiguration.getDeviceName(), APPNAME);
        verify(mockTextView, times(0)).setText(anyString());
    }

    @Test
    public void itShouldFailWhenResourceIdAvailableAndAppnameIsEmpty() throws Exception {
        subject.setStringFormattedText(mockTextView, mMockContext.getString(R.string.label_ews_confirm_connection_tip_upper), mockBaseContentConfiguration.getDeviceName(), "");
        verify(mockTextView, times(0)).setText(anyString());
    }

    @Test
    public void itShouldFailWhenResourceIdAndDeviceNameEmpty() throws Exception {
        when(mockBaseContentConfiguration.getDeviceName()).thenReturn(0);
        subject.setStringFormattedText(mockTextView, mMockContext.getString(R.string.label_ews_confirm_connection_tip_upper), mockBaseContentConfiguration.getDeviceName(), "");
        verify(mockTextView, times(0)).setText(anyString());
    }
}